package ar.edu.itba.pod.query5;

import ar.edu.itba.pod.Util;
import ar.edu.itba.pod.data.Ticket;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.MultiMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class groupByAverageMapper implements Mapper<String, Double, Integer, String>, HazelcastInstanceAware {

    private transient MultiMap<String, Double> averages;

    @Override
    public void setHazelcastInstance (HazelcastInstance hazelcastInstance){
        this.averages = hazelcastInstance.getMultiMap(Util.HAZELCAST_NAMESPACE_QUERY_5);
    }

    @Override
    public void map(String s, Double avg, Context<Integer, String> context) {
        context.emit(((avg.intValue()) / 100) * 100, s);
    }
}
