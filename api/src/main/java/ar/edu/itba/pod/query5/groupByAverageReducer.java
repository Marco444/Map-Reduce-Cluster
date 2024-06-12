package ar.edu.itba.pod.query5;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.ArrayList;
import java.util.List;

public class groupByAverageReducer implements ReducerFactory<Integer, String, List<String>> {
    @Override
    public Reducer<String, List<String>> newReducer(Integer integer) {
        return new Reducer<String, List<String>>() {

            private List<String> infractions;

            @Override
            public void beginReduce(){
                infractions = new ArrayList<>();
            }

            @Override
            public void reduce(String s) {
                infractions.add(s);
            }

            @Override
            public List<String> finalizeReduce() {
                return infractions;
            }
        };
    }
}
