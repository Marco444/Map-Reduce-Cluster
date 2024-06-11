package ar.edu.itba.pod.tpe2.client.query2;

import ar.edu.itba.pod.Util;
import ar.edu.itba.pod.data.Ticket;
import ar.edu.itba.pod.query1.FinesMapper;
import ar.edu.itba.pod.query1.FinesReducer;
import ar.edu.itba.pod.query2.Top3InfractionsByCityMapper;
import ar.edu.itba.pod.query2.Top3InfractionsByCityReducer;
import ar.edu.itba.pod.tpe2.client.QueryClient;
import ar.edu.itba.pod.tpe2.client.query1.TotalFinesByInfractions;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Top3InfractionsByCity extends QueryClient {

    Top3InfractionsByCity() {
        super();
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
