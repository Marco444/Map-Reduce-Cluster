package ar.edu.itba.pod.tpe2.client.query3;

import ar.edu.itba.pod.Constants;
import ar.edu.itba.pod.data.Ticket;
import ar.edu.itba.pod.query3.TopNEarningAgenciesMapper;
import ar.edu.itba.pod.query3.TopNEarningAgenciesReducer;
import ar.edu.itba.pod.tpe2.client.ClientArguments;
import ar.edu.itba.pod.tpe2.client.ExitCodes;
import ar.edu.itba.pod.tpe2.client.QueryClient;
import ar.edu.itba.pod.tpe2.client.Util;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

public class TopNEarningAgencies extends QueryClient {
    private final int n;

    public TopNEarningAgencies(int n) {
        super();
        this.n = n;
        execute();
    }

    @Override
    public void resolveQuery() throws ExecutionException, InterruptedException, IOException {
        final JobTracker jobTracker = getHz().getJobTracker(Constants.HAZELCAST_NAMESPACE);

        final KeyValueSource<String, Ticket> source = KeyValueSource.fromMultiMap(getHz().getMultiMap(Constants.HAZELCAST_NAMESPACE));

        Job<String, Ticket> job = jobTracker.newJob(source);

        Map<String, Double> reducedData = job
                .mapper(new TopNEarningAgenciesMapper())
                .reducer(new TopNEarningAgenciesReducer(n))
                .submit()
                .get();


        double sum = 0;
        Set<TopNEarningAgenciesResult> results = new TreeSet<TopNEarningAgenciesResult>();
        for (Map.Entry<String, Double> entry : reducedData.entrySet()) {
            sum += entry.getValue();
        }
        for (String key : reducedData.keySet()) {
            System.out.println(key + ": " + (reducedData.get(key) / sum) * 100 + "%");
            results.add(new TopNEarningAgenciesResult(key, (reducedData.get(key) / sum) * 100));
        }
        writeResults(results);
    }

    @Override
    public String getQueryNumber() {
        return "3";
    }

    @Override
    public String getQueryHeader() {
        return "Issuing Agency;Percentage";
    }

    public static void main(String[] args) {
        String nArgument = System.getProperty(ClientArguments.AGENCIES_NUMBER.getArgument());

        Util.requireArgument(nArgument, ClientArguments.AGENCIES_NUMBER);

        int n = 0;
        try {
            n = Integer.parseInt(nArgument);
        } catch (NumberFormatException e) {
            System.err.println("Argument " + ClientArguments.AGENCIES_NUMBER.getArgument() + " must be an integer");
            System.exit(ExitCodes.ILLEGAL_ARGUMENT.ordinal());
        }

        QueryClient queryClient = new TopNEarningAgencies(n);
    }
}
