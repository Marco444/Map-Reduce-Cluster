package ar.edu.itba.pod.query3;
import ar.edu.itba.pod.data.Agency;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class TopNEarningAgenciesReducer implements ReducerFactory<String, Double, Agency> {

    @Override
    public Reducer<Double, Agency> newReducer(String s) {
        return null;
    }
}
