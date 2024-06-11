package ar.edu.itba.pod.query2;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.*;

public class Top3InfractionsByCityReducer implements ReducerFactory<String, String, List<String>> {

    @Override
    public Reducer<String, List<String>> newReducer(String city) {
        return new Reducer<>() {
            private transient Map<String, Integer> infractionCounts;

            @Override
            public void beginReduce() {
                infractionCounts = new HashMap<>();
            }

            @Override
            public void reduce(String infractionCode) {
                infractionCounts.merge(infractionCode, 1, Integer::sum);
            }

            @Override
            public List<String> finalizeReduce() {
                PriorityQueue<Map.Entry<String, Integer>> topInfractions = new PriorityQueue<>(
                        Comparator.comparingInt(Map.Entry::getValue)
                );

                for (Map.Entry<String, Integer> entry : infractionCounts.entrySet()) {
                    topInfractions.add(entry);
                    if (topInfractions.size() > 3) {
                        topInfractions.poll();
                    }
                }

                List<String> result = new ArrayList<>();
                while (!topInfractions.isEmpty()) {
                    result.add(topInfractions.poll().getKey());
                }
                Collections.reverse(result); // To have the highest infraction count first
                return result;
            }
        };
    }
}
