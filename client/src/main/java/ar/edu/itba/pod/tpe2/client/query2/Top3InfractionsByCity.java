package ar.edu.itba.pod.tpe2.client.query2;

import ar.edu.itba.pod.Constants;
import ar.edu.itba.pod.data.Infractions;
import ar.edu.itba.pod.data.Ticket;
import ar.edu.itba.pod.query2.Top3InfractionsByCityMapper;
import ar.edu.itba.pod.query2.Top3InfractionsByCityReducer;
import ar.edu.itba.pod.tpe2.client.QueryClient;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Top3InfractionsByCity extends QueryClient {

    Top3InfractionsByCity() {
        super();
        execute();
    }
    @Override
    public void resolveQuery() throws ExecutionException, InterruptedException, IOException {

        getFileLogger().info("Inicio del trabajo map/reduce");

        final JobTracker jobTracker = getHz().getJobTracker(Constants.HAZELCAST_NAMESPACE);

        final KeyValueSource<String, Ticket> source = KeyValueSource.fromMultiMap(getHz().getMultiMap(Constants.HAZELCAST_NAMESPACE));

        Job<String, Ticket> job = jobTracker.newJob(source);

        Map<String, List<String>> reducedData = job
                .mapper(new Top3InfractionsByCityMapper())
                .reducer(new Top3InfractionsByCityReducer())
                .submit()
                .get();

        Map<String, Infractions> infractionsMap = getHz().getMap(Constants.HAZELCAST_NAMESPACE);
        List<Top3InfractionsByCityResult> results = new ArrayList<>();
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
            results.add(new Top3InfractionsByCityResult(entry.getKey(), infraction1, infraction2, infraction3));
        }
        writeResults(results);

        getFileLogger().info("Fin del trabajo map/reduce");
    }

    @Override
    public String getQueryNumber() {
        return "2";
    }

    @Override
    public String getQueryHeader() {
        return "county;InfractionTop1;InfractionTop2;InfractionTop3";
    }

    public static void main(String[] args) {
        QueryClient queryClient = new Top3InfractionsByCity();
    }
}
