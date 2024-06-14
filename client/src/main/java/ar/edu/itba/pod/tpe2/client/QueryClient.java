package ar.edu.itba.pod.tpe2.client;

import ar.edu.itba.pod.Constants;
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

import static ar.edu.itba.pod.tpe2.client.Util.requireArgument;

public abstract class QueryClient {
    private final static Logger logger = LoggerFactory.getLogger(QueryClient.class);
    private final static Logger fileLogger = LoggerFactory.getLogger("com.query.timeLogger");

    private HazelcastInstance hz;
    private String[] addresses;
    private Path outPath;
    private Path inPath;
    private Cities city;

    public QueryClient() {
        fileLogger.info("=============== Nueva Query ===============");
        try {
            checkArguments();
            this.hz = startHazelcastClient(this.addresses);
        } catch (IllegalArgumentException e) {
            System.err.println(Constants.ERROR_MESSAGE_INVALID_ARGUMENT + ": " + e.getMessage());
            logger.error(e.getMessage(), e);
            System.exit(ExitCodes.ILLEGAL_ARGUMENT.ordinal());
        } catch (IllegalStateException e) {
            System.err.println(Constants.ERROR_MESSAGE_SERVER_UNAVAILABLE + ": " + e.getMessage());
            logger.error(e.getMessage(), e);
            System.exit(ExitCodes.ILLEGAL_STATE.ordinal());
        } catch (Exception e) {
            System.err.println(Constants.ERROR_MESSAGE_UNEXPECTED_ERROR);
            logger.error(e.getMessage(), e);
            System.exit(ExitCodes.UNKNOWN_ERROR.ordinal());
        }
    }

    public void execute() {
        int status = ExitCodes.OK.ordinal();
        try {
            logger.info("=== START Query {} ===", getQueryNumber());

            logger.info("START - Data loading.");
            loadData();
            logger.info("END - Data loading.");

            logger.info("START - MapReduce job.");
            resolveQuery();
            logger.info("END - MapReduce job.");
        } catch (IllegalArgumentException e) {
            System.err.println(Constants.ERROR_MESSAGE_INVALID_ARGUMENT + ": " + e.getMessage());
            status = ExitCodes.ILLEGAL_ARGUMENT.ordinal();
        } catch (ExecutionException | InterruptedException e) {
            System.err.println("Oops! Something went wrong, try again!");
            logger.error(e.getMessage(), e);
            status = ExitCodes.EXECUTION_ERROR.ordinal();
        } catch (IOException e) {
            System.err.println(Constants.ERROR_MESSAGE_IO + ": " + e.getMessage());
            logger.error(e.getMessage(), e);
            status = ExitCodes.IO_ERROR.ordinal();
        } catch (IllegalStateException e) {
            System.err.println(Constants.ERROR_MESSAGE_SERVER_UNAVAILABLE + ": " + e.getMessage());
            logger.error(e.getMessage(), e);
            status = ExitCodes.ILLEGAL_STATE.ordinal();
        } catch (Exception e) {
            System.err.println(Constants.ERROR_MESSAGE_UNEXPECTED_ERROR);
            logger.error(e.getMessage(), e);
            status = ExitCodes.UNKNOWN_ERROR.ordinal();
        } finally {
            logger.info("START - Data destroy.");
            destroyData();
            logger.info("END - Data destroy.");

            if (this.hz != null) {
                this.hz.shutdown();
            }

            logger.info("=== END Query {} ===", getQueryNumber());
        }
        System.exit(status);
    }

    public HazelcastInstance getHz() {
        return hz;
    }

    private HazelcastInstance startHazelcastClient(String[] addresses) {
        ClientConfig clientConfig = new ClientConfig();
        GroupConfig groupConfig = new GroupConfig()
                .setName(Constants.HAZELCAST_GROUP_NAME)
                .setPassword(Constants.HAZELCAST_GROUP_PASSWORD);
        clientConfig.setGroupConfig(groupConfig);

        ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig();
        clientNetworkConfig.addAddress(addresses);
        clientConfig.setNetworkConfig(clientNetworkConfig);

        clientConfig.setProperty("hazelcast.logging.type", "none");

        return HazelcastClient.newHazelcastClient(clientConfig);
    }

    public void checkArguments() throws IllegalArgumentException {
        String addressesArgument = System.getProperty(ClientArguments.ADDRESS.getArgument());
        String inPathArgument = System.getProperty(ClientArguments.IN_PATH.getArgument());
        String outPathArgument = System.getProperty(ClientArguments.OUT_PATH.getArgument());
        String cityArgument = System.getProperty(ClientArguments.CITY.getArgument());

        requireArgument(addressesArgument, ClientArguments.ADDRESS);
        requireArgument(inPathArgument, ClientArguments.IN_PATH);
        requireArgument(outPathArgument, ClientArguments.OUT_PATH);
        requireArgument(cityArgument, ClientArguments.CITY);

        this.addresses = addressesArgument.split(";");

        inPath = Paths.get(inPathArgument);
        if (!Files.isDirectory(inPath)) {
            throw new IllegalArgumentException("Provided " + ClientArguments.IN_PATH.getArgument() + " is not a directory");
        }

        outPath = Paths.get(outPathArgument);
        if (!Files.isDirectory(outPath)) {
            throw new IllegalArgumentException("Provided " + ClientArguments.OUT_PATH.getArgument() + " is not a directory");
        }

        Optional<Cities> maybeCity = Cities.cityFromString(cityArgument);
        city = maybeCity.orElseThrow(() -> new IllegalArgumentException("Provided " + ClientArguments.CITY.getArgument() + " is not a valid city"));

        generateTicketsPath();
        generateInfractionsPath();
    }

    private Path generateTicketsPath() throws IllegalArgumentException {
        final String filename = Constants.TICKETS_FILENAME + city.getCity() + ".csv";

        Path path = Path.of(inPath.toString(), filename);
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("File '" + filename + "' does not exist in provided " + ClientArguments.IN_PATH.getArgument());
        }
        return path;
    }

    private Path generateInfractionsPath() throws IllegalArgumentException {

        final String filename = Constants.INFRACTIONS_FILENAME + city.getCity() + ".csv";

        Path path = Path.of(inPath.toString(), filename);
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("File '" + filename + "' does not exist in provided " + ClientArguments.IN_PATH.getArgument());
        }
        return path;
    }

    private void loadData() {
        if (hz == null) {
            logger.info("No Hazelcast instance found while tryng to load data.");
            return;
        }

        fileLogger.info("Inicio de la lectura del archivo");

        LoadTicketsFromFile loadTickets = new LoadTicketsFromFile(
                hz.getMultiMap(Constants.HAZELCAST_NAMESPACE), generateTicketsPath(), city
        );

        LoadInfractionsFromFile loadInfractions = new LoadInfractionsFromFile(
                hz.getMap(Constants.HAZELCAST_NAMESPACE), generateInfractionsPath()
        );

        try {
            loadTickets.load();
            loadInfractions.load();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            System.exit(ExitCodes.IO_ERROR.ordinal());
        }

        fileLogger.info("Fin de lectura del archivo");
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

    public Logger getFileLogger(){return fileLogger;}

    public String[] getAddresses() {
        return addresses;
    }

    public abstract void resolveQuery() throws ExecutionException, InterruptedException, IOException;

    public abstract String getQueryNumber();

    public abstract String getQueryHeader();

    private void destroyData() {
        if (hz == null) {
            logger.info("No Hazelcast instance. Doing nothing.");
            return;
        }

        hz.getMap(Constants.HAZELCAST_NAMESPACE).clear();
        hz.getMultiMap(Constants.HAZELCAST_NAMESPACE).clear();
    }
}