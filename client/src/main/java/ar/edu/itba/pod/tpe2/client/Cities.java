package ar.edu.itba.pod.tpe2.client;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Cities {
    NEW_YORK("NYC"),
    CHICAGO("CHI");

    final String city;

    Cities(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    private static final Map<String, Cities> stringToCitiesEnum =
            Stream.of(values())
                    .collect(Collectors.toMap(Cities::getCity, e -> e));

    public static Optional<Cities> cityFromString(final String symbol) {
        return Optional.ofNullable(stringToCitiesEnum.get(symbol));
    }
}
