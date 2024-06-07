package ar.edu.itba.pod.tpe2.client;

import ar.edu.itba.pod.Util;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public abstract class QueryClient {
    private final static Logger logger = LoggerFactory.getLogger(QueryClient.class);

    private HazelcastInstance hz;
    private String[] addresses;
    private Path outPath;
    private Path inPath;

    public QueryClient() {
        int status = 0;
        try {
            checkArguments();
            this.hz = startHazelcastClient(this.addresses);
            logger.warn("Hazelcast client started.");
            logger.info("Starting to load data.");
            loadData();
            logger.info("Finished loading data.");
            logger.info("Starting map/reduce job.");
            resolveQuery();
            logger.info("Finished map/reduce job.");
        } catch (IllegalArgumentException e) {
            System.err.println("Oops! Invalid arguments were sent:\n" + e.getMessage());
            status = 64;
        } catch (ExecutionException | InterruptedException e) {
            System.err.println("Oops! Something went wrong, try again!");
            logger.error(e.getMessage(), e);
            status = 130;
        } catch (IOException e) {
            System.err.println("Oops! Something went wrong when trying to write the results, try again!");
            logger.error(e.getMessage(), e);
            status = 74;
        } catch (IllegalStateException e) {
            System.err.println("Oops! We weren't able to connect to Hazelcast. Is the server running?");
            logger.error(e.getMessage(), e);
            status = 69;
        } catch (Exception e) {
            System.err.println("Oops! Something unexpected went wrong, try again!");
            logger.error(e.getMessage(), e);
            status = 127;
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

        if (addressesArgument == null) {
            errors.append("Argument 'addresses' must be provided\n");
        }
        if (inPathArgument == null) {
            errors.append("Argument 'inPath' must be provided\n");
        }
        if (outPathArgument == null) {
            errors.append("Argument 'outPath' must be provided\n");
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

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(errors.toString());
        }
        System.out.println(Arrays.toString(addresses));
    }

    private void loadData() {
        if (hz == null) {
            return;
        }

        try (
                ExecutorService service = Executors.newCachedThreadPool()
        ) {

            service.shutdown();
            service.awaitTermination(Util.SYSTEM_TIMEOUT, Util.SYSTEM_TIMEOUT_UNIT);
        } catch (InterruptedException e) {
            logger.error("Interrupted load of data");
            System.exit(2);
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


    private static class LoadTicketsRunnable implements Runnable {

        @Override
        public void run() {

        }
    }
}