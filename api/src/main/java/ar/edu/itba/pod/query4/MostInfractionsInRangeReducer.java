package ar.edu.itba.pod.query4;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.HashMap;
import java.util.Map;

public class MostInfractionsInRangeReducer implements ReducerFactory<String, String, String> {

    @Override
    public Reducer<String, String> newReducer(String key) {
        return new Reducer<String, String>() {
            private Map<String, Integer> plateCount;

            @Override
            public void beginReduce() {
                plateCount = new HashMap<>();
            }

            @Override
            public void reduce(String plate) {
                plateCount.merge(plate, 1, Integer::sum);
            }

            @Override
            public String finalizeReduce() {
                return plateCount.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .map(entry -> entry.getKey() + ";" + entry.getValue())
                        .orElse("");
            }
        };
    }
}
