package ar.edu.itba.pod.query5;

import ar.edu.itba.pod.Constants;
import ar.edu.itba.pod.data.Infractions;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.MultiMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.Map;

public class groupByAverageMapper implements Mapper<String, Double, Integer, String>, HazelcastInstanceAware {

    private transient MultiMap<String, Double> averages;
    private transient Map<String, Infractions> infractions;

    @Override
    public void setHazelcastInstance (HazelcastInstance hazelcastInstance){
        this.averages = hazelcastInstance.getMultiMap(Constants.HAZELCAST_NAMESPACE_QUERY_5);
        this.infractions = hazelcastInstance.getMap(Constants.HAZELCAST_NAMESPACE);
    }

    @Override
    public void map(String s, Double avg, Context<Integer, String> context) {
        context.emit(((avg.intValue()) / 100) * 100, infractions.get(s).getDescription());
    }
}
