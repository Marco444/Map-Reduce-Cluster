package ar.edu.itba.pod.query1;

import ar.edu.itba.pod.Util;
import ar.edu.itba.pod.data.Infractions;
import ar.edu.itba.pod.data.Ticket;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.MultiMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.io.Serial;
import java.util.Map;

public class FinesMapper implements Mapper<String, Ticket, String, Integer>, HazelcastInstanceAware {
    @Serial
    private static final long serialVersionUID = 1L; // Add a unique serialVersionUID

    private transient MultiMap<String, Infractions> infractions;

    public FinesMapper() {
    }


    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.infractions = hazelcastInstance.getMultiMap(Util.HAZELCAST_NAMESPACE);
    }

    @Override
    public void map(String str, Ticket tickets, Context<String, Integer> context) {
        if (!infractions.containsKey(str)) {
            return;
        }

        context.emit(tickets.getInfractionCode(), 1);
    }
}
