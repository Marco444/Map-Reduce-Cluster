package ar.edu.itba.pod.tpe2.client.query1;

import ar.edu.itba.pod.Constants;
import ar.edu.itba.pod.data.Infractions;
import ar.edu.itba.pod.data.Ticket;
import ar.edu.itba.pod.query1.FinesMapper;
import ar.edu.itba.pod.query1.FinesReducer;
import ar.edu.itba.pod.tpe2.client.QueryClient;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class TotalFinesByInfractions extends QueryClient {


    public TotalFinesByInfractions() {
        super();
        execute();
    }

    @Override
    public void resolveQuery() throws ExecutionException, InterruptedException, IOException {

        getFileLogger().info("Inicio del trabajo map/reduce");

        final JobTracker jobTracker = getHz().getJobTracker(Constants.HAZELCAST_NAMESPACE);

        final KeyValueSource<String, Ticket> source = KeyValueSource.fromMultiMap(getHz().getMultiMap(Constants.HAZELCAST_NAMESPACE));

        Job<String, Ticket> job = jobTracker.newJob(source);

        Map<String, Integer> reducedData = job
                .mapper(new FinesMapper())
                .reducer(new FinesReducer())
                .submit()
                .get();

        Map<String, Infractions> infractionsMap = getHz().getMap(Constants.HAZELCAST_NAMESPACE);
        List<TotalFinesByInfractionsResult> results = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : reducedData.entrySet()) {

            String key = infractionsMap.containsKey(entry.getKey())
                    ? infractionsMap.get(entry.getKey()).getDescription()
                    : "";

            results.add(new TotalFinesByInfractionsResult(key, entry.getValue()));
        }
        writeResults(results);

        getFileLogger().info("Fin del trabajo map/reduce");
    }

    @Override
    public String getQueryNumber() {
        return "1";
    }

    @Override
    public String getQueryHeader() {
        return "Infraction;Tickets";
    }

    public static void main(String[] args) {
        QueryClient queryClient = new TotalFinesByInfractions();
    }
}
