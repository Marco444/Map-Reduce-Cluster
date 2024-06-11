package ar.edu.itba.pod.tpe2.client;

import ar.edu.itba.pod.Util;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class QueryClient {
    private final static Logger logger = LoggerFactory.getLogger(QueryClient.class);

    private HazelcastInstance hz;
    private String[] addresses;
    private Path outPath;
    private Path inPath;
    private Cities city;

    public QueryClient() {
        try {
            checkArguments();
            this.hz = startHazelcastClient(this.addresses);
        } catch (IllegalArgumentException e) {
            System.err.println("Oops! Invalid arguments were sent:\n" + e.getMessage());
            System.exit(ExitCodes.ILLEGAL_ARGUMENT.ordinal());
        } catch (IllegalStateException e) {
            System.err.println("Oops! We weren't able to connect to Hazelcast. Is the server running?");
            logger.error(e.getMessage(), e);
            System.exit(ExitCodes.ILLEGAL_STATE.ordinal());
        } catch (Exception e) {
            System.err.println("Oops! Something unexpected went wrong, try again!");
            logger.error(e.getMessage(), e);
            System.exit(ExitCodes.UNKNOWN_ERROR.ordinal());
        }
    }

    public void execute() {
        int status = ExitCodes.OK.ordinal();
        try {
            logger.warn("Hazelcast client started.");
            logger.info("Starting to load data.");
            loadData();
            logger.info("Finished loading data.");
            logger.info("Starting map/reduce job.");
            resolveQuery();
            logger.info("Finished map/reduce job.");
        } catch (IllegalArgumentException e) {
            System.err.println("Oops! Invalid arguments were sent:\n" + e.getMessage());
            status = ExitCodes.ILLEGAL_ARGUMENT.ordinal();
        } catch (ExecutionException | InterruptedException e) {
            System.err.println("Oops! Something went wrong, try again!");
            logger.error(e.getMessage(), e);
            status = ExitCodes.EXECUTION_ERROR.ordinal();
        } catch (IOException e) {
            System.err.println("Oops! Something went wrong when trying to write the results, try again!");
            logger.error(e.getMessage(), e);
            status = ExitCodes.IO_ERROR.ordinal();
        } catch (IllegalStateException e) {
            System.err.println("Oops! We weren't able to connect to Hazelcast. Is the server running?");
            logger.error(e.getMessage(), e);
            status = ExitCodes.ILLEGAL_STATE.ordinal();
        } catch (Exception e) {
            System.err.println("Oops! Something unexpected went wrong, try again!");
            logger.error(e.getMessage(), e);
            status = ExitCodes.UNKNOWN_ERROR.ordinal();
        } finally {
            logger.info("Destroying data.");
            destroyData();
            logger.info("All data was destroyed.");
            if (this.hz != null) {
                this.hz.shutdown();
            }
        }
        System.exit(status);
    }

    public HazelcastInstance getHz() {
        return hz;
    }

    private HazelcastInstance startHazelcastClient(String[] addresses) {
        ClientConfig clientConfig = new ClientConfig();
        GroupConfig groupConfig = new GroupConfig()
                .setName(Util.HAZELCAST_GROUP_NAME)
                .setPassword(Util.HAZELCAST_GROUP_PASSWORD);
        clientConfig.setGroupConfig(groupConfig);

        ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig();
        clientNetworkConfig.addAddress(addresses);
        clientConfig.setNetworkConfig(clientNetworkConfig);

        clientConfig.setProperty("hazelcast.logging.type", "none");

        return HazelcastClient.newHazelcastClient(clientConfig);
    }

    public void checkArguments() throws IllegalArgumentException {
        StringBuilder errors = new StringBuilder();
        String addressesArgument = System.getProperty("addresses");
        String inPathArgument = System.getProperty("inPath");
        String outPathArgument = System.getProperty("outPath");
        String cityArgument = System.getProperty("city");

        if (addressesArgument == null) {
            errors.append("Argument 'addresses' must be provided\n");
        }
        if (inPathArgument == null) {
            errors.append("Argument 'inPath' must be provided\n");
        }
        if (outPathArgument == null) {
            errors.append("Argument 'outPath' must be provided\n");
        }
        if (cityArgument == null) {
            errors.append("Argument 'city' must be provided\n");
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(errors.toString());
        }

        this.addresses = addressesArgument.split(";");

        inPath = Paths.get(inPathArgument);
        if (!Files.isDirectory(inPath)) {
            errors.append("Provided 'inPath' is not a directory\n");
        }
        outPath = Paths.get(outPathArgument);
        if (!Files.isDirectory(outPath)) {
            errors.append("Provided 'outPath' is not a directory\n");
        }

        Optional<Cities> maybeCity = Cities.cityFromString(cityArgument);
        if (!maybeCity.isPresent()) {
            errors.append("Provided 'city' is not a valid city\n");
        } else {
            city = maybeCity.get();
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(errors.toString());
        }

        generateTicketsPath();
        generateInfractionsPath();


    }

    private Path generateTicketsPath() throws IllegalArgumentException {
        final String filename = Util.TICKETS_FILENAME + city.getCity() + ".csv";

        Path path = Path.of(inPath.toString(), filename);
        if (!Files.exists(path)) {
            System.out.println(path);
            throw new IllegalArgumentException("File '" + filename + "' does not exist in provided inPath");
        }
        return path;
    }

    private Path generateInfractionsPath() throws IllegalArgumentException {

        final String filename = Util.INFRACTIONS_FILENAME + city.getCity() + ".csv";

        Path path = Path.of(inPath.toString(), filename);
        if (!Files.exists(path)) {
            System.out.println(path);
            throw new IllegalArgumentException("File '" + filename + "' does not exist in provided inPath");
        }
        return path;
    }

    private void loadData() {
        if (hz == null) {
            logger.error("Tried to load data with NULL Hazelcast instance.");
            return;
        }

        try (ExecutorService service = Executors.newCachedThreadPool()) {
            service.submit(new LoadTicketsRunnable(
                    hz.getMultiMap(Util.HAZELCAST_NAMESPACE), generateTicketsPath(), city
            ));

            service.submit(new LoadInfractionsRunnable(
                    hz.getMap(Util.HAZELCAST_NAMESPACE), generateInfractionsPath()
            ));

            service.shutdown();
            boolean finished = service.awaitTermination(Util.SYSTEM_TIMEOUT, Util.SYSTEM_TIMEOUT_UNIT);
            if (!finished) {
                logger.error(
                        "Load data service timed out after {} {} before reading all data.",
                        Util.SYSTEM_TIMEOUT,
                        Util.SYSTEM_TIMEOUT_UNIT
                );
            }
        } catch (InterruptedException e) {
            logger.error("Interrupted load of data");
            System.exit(2);
        }
    }

    public void writeResults(Collection<? extends Result> results) throws IOException {
        Path queryPath = outPath.resolve("query" + getQueryNumber() + ".csv");
        try (BufferedWriter writer = Files.newBufferedWriter(
                queryPath, StandardOpenOption.WRITE,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        )) {
            writer.write(getQueryHeader());
            writer.newLine();
            for (Result result : results) {
                writer.write(result.toString());
                writer.newLine();
            }
        }
    }

    public String[] getAddresses() {
        return addresses;
    }

    public abstract void resolveQuery() throws ExecutionException, InterruptedException, IOException;

    public abstract String getQueryNumber();

    public abstract String getQueryHeader();

    private void destroyData() {
        if (hz == null) {
            return;
        }

        hz.getMap(Util.HAZELCAST_NAMESPACE).clear();
        hz.getMultiMap(Util.HAZELCAST_NAMESPACE).clear();
    }
}