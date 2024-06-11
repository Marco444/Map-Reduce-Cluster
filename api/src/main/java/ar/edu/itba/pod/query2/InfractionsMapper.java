package ar.edu.itba.pod.query2;

import ar.edu.itba.pod.Util;
import ar.edu.itba.pod.data.Infractions;
import ar.edu.itba.pod.data.Ticket;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.io.Serial;
import java.util.Map;

public class InfractionsMapper implements Mapper<String, Infractions, String, Integer>, HazelcastInstanceAware {
    @Serial
    private static final long serialVersionUID = 1L; // Add a unique serialVersionUID

    private transient Map<String, Infractions> infractions;

    private InfractionsMapper() {
    }


    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.infractions = hazelcastInstance.getMap(Util.HAZELCAST_NAMESPACE);
    }


    @Override
    public void map(String city, Infractions infraction, Context<String, Integer> context) {
        context.emit(infraction.getCode(), 1);
    }
}