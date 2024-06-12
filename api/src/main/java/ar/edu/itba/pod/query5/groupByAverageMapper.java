package ar.edu.itba.pod.query5;

import ar.edu.itba.pod.Util;
import ar.edu.itba.pod.data.Infractions;
import ar.edu.itba.pod.data.Ticket;
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
        this.averages = hazelcastInstance.getMultiMap(Util.HAZELCAST_NAMESPACE_QUERY_5);
        this.infractions = hazelcastInstance.getMap(Util.HAZELCAST_NAMESPACE);
    }

    @Override
    public void map(String s, Double avg, Context<Integer, String> context) {
        context.emit(((avg.intValue()) / 100) * 100, infractions.get(s).getDescription());
    }
}
