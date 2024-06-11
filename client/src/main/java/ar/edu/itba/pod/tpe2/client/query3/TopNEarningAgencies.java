package ar.edu.itba.pod.tpe2.client.query3;

import ar.edu.itba.pod.Util;
import ar.edu.itba.pod.data.Agency;
import ar.edu.itba.pod.data.Ticket;
import ar.edu.itba.pod.query2.Top3InfractionsByCityMapper;
import ar.edu.itba.pod.query2.Top3InfractionsByCityReducer;
import ar.edu.itba.pod.query3.TopNEarningAgenciesMapper;
import ar.edu.itba.pod.query3.TopNEarningAgenciesReducer;
import ar.edu.itba.pod.tpe2.client.QueryClient;
import ar.edu.itba.pod.tpe2.client.query2.Top3InfractionsByCity;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class TopNEarningAgencies extends QueryClient {
    private final int n;

    public TopNEarningAgencies(int n) {
        this.n = n;
    }

    @Override
    public void resolveQuery() throws ExecutionException, InterruptedException, IOException {
        final JobTracker jobTracker = getHz().getJobTracker(Util.HAZELCAST_NAMESPACE);

        final KeyValueSource<String, Ticket> source = KeyValueSource.fromMultiMap(getHz().getMultiMap(Util.HAZELCAST_NAMESPACE));

        Job<String, Ticket> job = jobTracker.newJob(source);

        Map<String, Map<String, Agency>> reducedData = job
                .mapper(new TopNEarningAgenciesMapper())
                .reducer(new TopNEarningAgenciesReducer(n))
                .submit()
                .get();
    }

    @Override
    public String getQueryNumber() {
        return "query3";
    }

    @Override
    public String getQueryHeader() {
        return "Issuing Agency;Percentage";
    }

    public static void main(String[] args) {
        int n = 5;
        QueryClient queryClient = new TopNEarningAgencies(n);
    }
}
