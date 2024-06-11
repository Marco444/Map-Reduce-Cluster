package ar.edu.itba.pod.tpe2.client.query4;

import ar.edu.itba.pod.Util;
import ar.edu.itba.pod.data.Infractions;
import ar.edu.itba.pod.data.Ticket;
import ar.edu.itba.pod.query2.Top3InfractionsByCityMapper;
import ar.edu.itba.pod.query2.Top3InfractionsByCityReducer;
import ar.edu.itba.pod.tpe2.client.QueryClient;
import ar.edu.itba.pod.tpe2.client.query2.Top3InfractionsByCityResult;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MostInfractionsInRange extends QueryClient {
    private static final DateTimeFormatter ARGUMENT_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yy");
    private LocalDate from;
    private LocalDate to;

    MostInfractionsInRange(LocalDate from, LocalDate to) {
        super();
        this.from = from;
        this.to = to;

        execute();
    }

    @Override
    public void resolveQuery() throws ExecutionException, InterruptedException, IOException {
        final JobTracker jobTracker = getHz().getJobTracker(Util.HAZELCAST_NAMESPACE);

        final KeyValueSource<String, Ticket> source = KeyValueSource.fromMultiMap(getHz().getMultiMap(Util.HAZELCAST_NAMESPACE));

        Job<String, Ticket> job = jobTracker.newJob(source);

        Map<String, List<String>> reducedData = job
                .mapper(new Top3InfractionsByCityMapper())
                .reducer(new Top3InfractionsByCityReducer())
                .submit()
                .get();

        Map<String, Infractions> infractionsMap = getHz().getMap(Util.HAZELCAST_NAMESPACE);
        List<MostInfractionsInRangeResult> results = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : reducedData.entrySet()) {

            String infraction1 = !entry.getValue().isEmpty() && infractionsMap.containsKey(entry.getValue().get(0))
                    ? infractionsMap.get(entry.getValue().get(0)).getDescription()
                    : "";

            String infraction2 = entry.getValue().size() > 1 && infractionsMap.containsKey(entry.getValue().get(1))
                    ? infractionsMap.get(entry.getValue().get(1)).getDescription()
                    : "";

            String infraction3 = entry.getValue().size() > 2 && infractionsMap.containsKey(entry.getValue().get(2))
                    ? infractionsMap.get(entry.getValue().get(2)).getDescription()
                    : "";
            results.add(new MostInfractionsInRangeResult(entry.getKey(), infraction1, infraction2, infraction3));
        }
        writeResults(results);
    }

    @Override
    public String getQueryNumber() {
        return "4";
    }

    @Override
    public String getQueryHeader() {
        return "County;Plate;Tickets";
    }

    public static void main(String[] args) {
        StringBuilder errors = new StringBuilder();
        String fromArgument = System.getProperty("from");
        String toArgument = System.getProperty("to");

        if (fromArgument == null) {
            errors.append("Argument 'from' must be provided with a valid date\n");
        }
        if (toArgument == null) {
            errors.append("Argument 'to' must be provided with a valid date\n");
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(errors.toString());
        }

        LocalDate from = LocalDate.parse(fromArgument, ARGUMENT_DATE_FORMATTER);
        LocalDate to = LocalDate.parse(toArgument, ARGUMENT_DATE_FORMATTER);

        QueryClient queryClient = new MostInfractionsInRange(from, to);
    }
}
