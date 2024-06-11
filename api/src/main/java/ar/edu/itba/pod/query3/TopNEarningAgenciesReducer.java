package ar.edu.itba.pod.query3;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class TopNEarningAgenciesReducer implements ReducerFactory<String, Double, Double> {
    private final int N;

    public TopNEarningAgenciesReducer(int N) {
        this.N = N;
    }

    @Override
    public Reducer<Double, Double> newReducer(String key) {
        return new Reducer<Double, Double>() {

            private double totalEarnings;

            @Override
            public void beginReduce() {
                totalEarnings = 0;
            }

            @Override
            public void reduce(Double value) {
                totalEarnings += value;
            }

            @Override
            public Double finalizeReduce() {
                return totalEarnings;
            }
        };
    }
}
