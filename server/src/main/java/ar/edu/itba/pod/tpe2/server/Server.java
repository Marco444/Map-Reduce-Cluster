package ar.edu.itba.pod.tpe2.server;

import ar.edu.itba.pod.Constants;
import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        final String addressArgument = System.getProperty("address");

        final Config config = new Config();
        final GroupConfig groupConfig = new GroupConfig()
                .setName(Constants.HAZELCAST_GROUP_NAME)
                .setPassword(Constants.HAZELCAST_GROUP_PASSWORD);
        config.setGroupConfig(groupConfig);

        final JoinConfig joinConfig = new JoinConfig().setMulticastConfig(new MulticastConfig());
        final NetworkConfig networkConfig = new NetworkConfig().setJoin(joinConfig);
        if (addressArgument != null) {
            InterfacesConfig interfacesConfig = new InterfacesConfig()
                    .setInterfaces(Collections.singletonList(addressArgument))
                    .setEnabled(true);
            networkConfig.setInterfaces(interfacesConfig);
            logger.debug("Found subnet mask: {}", addressArgument);
        }

        config.setNetworkConfig(networkConfig);

        config.setProperty("hazelcast.logging.type", "none");

        config.getMultiMapConfig(Constants.HAZELCAST_NAMESPACE)
                .setValueCollectionType(MultiMapConfig.ValueCollectionType.LIST);

        // Start cluster
        final HazelcastInstance instance = Hazelcast.newHazelcastInstance(config);
        logger.info("Cluster discoverable on {}", instance.getCluster().getLocalMember().getAddress());
    }
}