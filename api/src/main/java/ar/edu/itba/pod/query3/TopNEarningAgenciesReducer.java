package ar.edu.itba.pod.query3;
import ar.edu.itba.pod.data.Agency;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class TopNEarningAgenciesReducer implements ReducerFactory<String, Double, Map<String, Agency>> {
    private final int N;

    public TopNEarningAgenciesReducer(int N) {
        this.N = N;
    }

    @Override
    public Reducer<Double, Map<String, Agency>> newReducer(String key) {
        return new Reducer<Double, Map<String, Agency>>() {
            private Map<String, Double> earnings;
            private double totalEarnings;

            @Override
            public void beginReduce() {
                earnings = new HashMap<>();
                totalEarnings = 0;
            }

            @Override
            public void reduce(Double value) {
                earnings.merge(key, value, Double::sum);
                totalEarnings += value;
            }

            @Override
            public Map<String, Agency> finalizeReduce() {
                PriorityQueue<Map.Entry<String, Double>> pq = new PriorityQueue<>(
                        (a, b) -> Double.compare(b.getValue(), a.getValue())
                );

                for (Map.Entry<String, Double> entry : earnings.entrySet()) {
                    pq.offer(entry);
                }

                Map<String, Agency> result = new HashMap<>();
                for (int i = 0; i < N && !pq.isEmpty(); i++) {
                    Map.Entry<String, Double> entry = pq.poll();
                    result.put(entry.getKey(), new Agency(entry.getKey(), entry.getValue() / totalEarnings * 100));
                }

                return result;
            }
        };
    }
}
