package ar.edu.itba.pod.tpe2.client.query3;

import ar.edu.itba.pod.tpe2.client.QueryClient;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class TopNEarningAgencies extends QueryClient {
    public TopNEarningAgencies() {
    }

    @Override
    public void resolveQuery() throws ExecutionException, InterruptedException, IOException {

    }

    @Override
    public String getQueryNumber() {
        return "query3";
    }

    @Override
    public String getQueryHeader() {
        return "Issuing Agency;Percentage";
    }
}
