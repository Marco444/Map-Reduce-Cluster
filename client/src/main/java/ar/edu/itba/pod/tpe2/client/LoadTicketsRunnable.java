package ar.edu.itba.pod.tpe2.client;

import ar.edu.itba.pod.data.Ticket;
import com.hazelcast.core.MultiMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class LoadTicketsRunnable implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(LoadTicketsRunnable.class);

    private final MultiMap<String, Ticket> multiMap;
    private final Path filePath;
    private final Cities city;

    public LoadTicketsRunnable(MultiMap<String, Ticket> multiMap, Path filePath, Cities city) {
        this.multiMap = multiMap;
        this.filePath = filePath;
        this.city = city;
    }

    @Override
    public void run() {
        try (Stream<String> lines = Files.lines(filePath).skip(1).parallel()) {
            lines.forEach(line -> {
                String[] fields = line.split(";");

                switch (city) {
                    case CHICAGO -> multiMap.put(
                            fields[1], Ticket.of(
                                    fields[1],
                                    fields[0].substring(0, 10), // Bye, bye time
                                    fields[2],
                                    Double.parseDouble(fields[4]),
                                    fields[5],
                                    fields[3]
                            )
                    );
                    case NEW_YORK -> multiMap.put(
                            fields[0], Ticket.of(
                                    fields[0], fields[1], fields[2], Double.parseDouble(fields[3]), fields[4], fields[5]
                            )
                    );
                    default -> throw new IllegalArgumentException("Invalid city: " + city);
                }

            });
        } catch (IOException e) {
            logger.error("Could not load data :(", e);
        }
    }
}
