package ar.edu.itba.pod.tpe2.client;

import ar.edu.itba.pod.data.Infractions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

public class LoadInfractionsRunnable implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(LoadInfractionsRunnable.class);

    private final Map<String, Infractions> map;
    private final Path filePath;

    public LoadInfractionsRunnable(Map<String, Infractions> map, Path filePath) {
        this.map = map;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        try (Stream<String> lines = Files.lines(filePath).skip(1).parallel()) {
            lines.forEach(line -> {
                String[] fields = line.split(";");
                map.put(fields[0], Infractions.of(fields[0], fields[1]));
            });
        } catch (IOException e) {
            logger.error("Could not load infractions data :(", e);
        }
    }
}
