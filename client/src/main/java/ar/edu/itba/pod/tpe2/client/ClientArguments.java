package ar.edu.itba.pod.tpe2.client;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ClientArguments {
    ADDRESS("addresses"),
    IN_PATH("inPath"),
    OUT_PATH("outPath"),
    CITY("city"),
    FROM("from"),
    TO("to"),
    AGENCIES_NUMBER("n");

    private final String argument;

    ClientArguments(String argument) {
        this.argument = argument;
    }

    public String getArgument() {
        return argument;
    }

    private static final Map<String, ClientArguments> stringToCheckInClientArgumentsEnum =
            Stream.of(values())
                    .collect(Collectors.toMap(ClientArguments::getArgument, e -> e));

    public static Optional<ClientArguments> checkInClientArgumentsFromString(final String symbol) {
        return Optional.ofNullable(stringToCheckInClientArgumentsEnum.get(symbol));
    }
}
