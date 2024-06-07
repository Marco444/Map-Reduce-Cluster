package ar.edu.itba.pod.tpe2.client.query1;

import ar.edu.itba.pod.Util;
import ar.edu.itba.pod.data.LongTuple;
import ar.edu.itba.pod.data.Tickets;
import ar.edu.itba.pod.query1.FinesMapper;
import ar.edu.itba.pod.query1.FinesReducer;
import ar.edu.itba.pod.tpe2.client.QueryClient;
import com.hazelcast.internal.networking.spinning.SpinningOutputThread;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.io.IOException;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

public class TotalFinesByInfractions extends QueryClient {



    public TotalFinesByInfractions() {
        super();
    }

    @Override
    public void resolveQuery() throws ExecutionException, InterruptedException, IOException {
        final JobTracker jobTracker = getHz().getJobTracker(Util.HAZELCAST_NAMESPACE);

        final KeyValueSource<Integer, Tickets> source = KeyValueSource.fromMultiMap(getHz().getMultiMap(Util.HAZELCAST_NAMESPACE));

        Job<Integer, Tickets> job = jobTracker.newJob(source);

        Map<LongTuple, Integer> reducedData = job
                .mapper(new FinesMapper())
                .reducer(new FinesReducer())
                .submit()
                .get();

        Map<Integer, Tickets> tickets = getHz().getMap(Util.HAZELCAST_NAMESPACE);

    }

    @Override
    public String getQueryNumber() {
        return "1";
    }

    @Override
    public String getQueryHeader() {
        return "infractions;total_fines";
    }

    public static void main(String[] args) {
        QueryClient queryClient = new TotalFinesByInfractions();
    }
}
