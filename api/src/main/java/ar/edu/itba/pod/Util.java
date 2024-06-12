package ar.edu.itba.pod;

import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class Util {
    public final static String HAZELCAST_GROUP_NAME = "g";
    public final static String HAZELCAST_GROUP_PASSWORD = "hadbojhcvhdfx95+wxe68";
    public final static String HAZELCAST_NAMESPACE = "g5-namespace";

    public final static String HAZELCAST_NAMESPACE_QUERY_5 = "g5-namespace-aux";

    public static final int SYSTEM_TIMEOUT = 1;

    public static final TimeUnit SYSTEM_TIMEOUT_UNIT = TimeUnit.MINUTES;

    public static final String TICKETS_FILENAME = "tickets";
    public static final String INFRACTIONS_FILENAME = "infractions";

    public static final char CSV_DELIMITER = ';';
}
