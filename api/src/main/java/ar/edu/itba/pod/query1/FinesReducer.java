package ar.edu.itba.pod.query1;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class FinesReducer implements ReducerFactory<String, Integer, Integer> {
    @Override
    public Reducer<Integer, Integer> newReducer(String violationCode) {
        return new Reducer<>() {
            private int sum;

            @Override
            public void beginReduce () {
                sum = 0;
            }

            @Override
            public void reduce(Integer value) {
                sum += value;
            }

            @Override
            public Integer finalizeReduce() {
                return sum;
            }
        };
    }
}