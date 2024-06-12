package ar.edu.itba.pod.query5;

import ar.edu.itba.pod.Constants;
import ar.edu.itba.pod.data.Ticket;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.MultiMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;


public class InfractionsToAverageMapper implements Mapper<String, Ticket, String, Double>, HazelcastInstanceAware {

    private transient MultiMap<String, Ticket> tickets;

    @Override
    public void setHazelcastInstance (HazelcastInstance hazelcastInstance){
        this.tickets = hazelcastInstance.getMultiMap(Constants.HAZELCAST_NAMESPACE);
    }

    @Override
    public void map(String s, Ticket ticket, Context<String, Double> context) {
        context.emit(ticket.getInfractionCode(), ticket.getFineAmount());
    }
}