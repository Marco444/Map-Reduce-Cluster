package ar.edu.itba.pod;

public class Constants {
    public final static String HAZELCAST_GROUP_NAME = "g";
    public final static String HAZELCAST_GROUP_PASSWORD = "hadbojhcvhdfx95+wxe68";
    public final static String HAZELCAST_NAMESPACE = "g5-namespace";

    public final static String HAZELCAST_NAMESPACE_QUERY_5 = "g5-namespace-aux";

    public static final String TICKETS_FILENAME = "tickets";
    public static final String INFRACTIONS_FILENAME = "infractions";

    public static final char CSV_DELIMITER = ';';

    public static final String ERROR_MESSAGE_INVALID_ARGUMENT = "There's something wrong with the provided arguments";
    public static final String ERROR_MESSAGE_IO = "Read/Write error";
    public static final String ERROR_MESSAGE_SERVER_UNAVAILABLE = "Could not connect to server. Is it running? Did you provide the right address?";
    public static final String ERROR_MESSAGE_UNEXPECTED_ERROR = "Something went wrong";
}
