package ar.edu.itba.pod;

import java.time.format.DateTimeFormatter;

public class Util {
    public final static String HAZELCAST_GROUP_NAME = "g";
    public final static String HAZELCAST_GROUP_PASSWORD = "hadbojhcvhdfx95+wxe68";
    public final static String HAZELCAST_NAMESPACE = "g5-namespace";

    public final static String HAZELCAST_NAMESPACE_2 = "g5-namespace-aux";

    public final static DateTimeFormatter INPUT_DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final int SYSTEM_TIMEOUT = 1;
}
