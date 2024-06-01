package ar.edu.itba.pod.query1;

import ar.edu.itba.pod.Util;
import ar.edu.itba.pod.data.LongTuple;
import ar.edu.itba.pod.data.Tickets;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.Map;

public class FinesMapper implements Mapper<Integer, Tickets, LongTuple, Integer>, HazelcastInstanceAware {
    private transient Map<Integer, Tickets> stations;

    public FinesMapper() {
    }


    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.stations = hazelcastInstance.getMap(Util.HAZELCAST_NAMESPACE);
    }

    @Override
    public void map(Integer integer, Tickets tickets, Context<LongTuple, Integer> context) {

    }
}
