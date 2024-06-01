package ar.edu.itba.pod.tpe2.client.query1;

import ar.edu.itba.pod.Util;
import ar.edu.itba.pod.tpe2.client.QueryClient;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.io.IOException;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

public class TotalFinesByInfractions extends QueryClient {
    @Override
    public void resolveQuery() throws ExecutionException, InterruptedException, IOException {

    }

    @Override
    public String getQueryNumber() {
        return "1";
    }

    @Override
    public String getQueryHeader() {
        return "infractions;total_fines";
    }
}
