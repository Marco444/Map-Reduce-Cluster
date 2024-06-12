package ar.edu.itba.pod.query5;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.Map;

public class InfractionsToAverageReducer implements ReducerFactory<String, Double, Double> {

    @Override
    public Reducer<Double, Double> newReducer(String s) {
        return new Reducer<>() {

            int count;
            double sum;

            @Override
            public void beginReduce(){
                count = 0;
                sum = 0;
            }

            @Override
            public void reduce(Double value) {
                sum += value;
                count++;
            }

            @Override
            public Double finalizeReduce() {
                return sum / (double) count;
            }
        };
    }
}
