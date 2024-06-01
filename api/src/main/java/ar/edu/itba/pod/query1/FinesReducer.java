package ar.edu.itba.pod.query1;

import ar.edu.itba.pod.data.LongTuple;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class FinesReducer implements ReducerFactory<LongTuple, Integer, Integer> {
    @Override
    public Reducer<Integer, Integer> newReducer(LongTuple integerPair) {
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