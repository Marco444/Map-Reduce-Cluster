package ar.edu.itba.pod.query2;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.*;

public class Top3InfractionsByCityReducer implements ReducerFactory<String, Integer, List<String>> {

    @Override
    public Reducer<Integer, List<String>> newReducer(String s) {
        return new Reducer<>() {
            private transient PriorityQueue<Map.Entry<String, Integer>> topCities;

            @Override
            public void beginReduce() {
                topCities = new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getValue));
            }

            @Override
            public void reduce(Integer value) {
                topCities.add(new AbstractMap.SimpleEntry<>(s, value));
                if (topCities.size() > 3) {
                    topCities.poll();
                }
            }

            @Override
            public List<String> finalizeReduce() {
                List<String> result = new ArrayList<>();
                while (!topCities.isEmpty()) {
                    result.add(topCities.poll().getKey());
                }
                Collections.reverse(result); // To have the highest infraction count first
                return result;
            }
        };
    }
}
