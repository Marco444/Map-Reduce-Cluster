package ar.edu.itba.pod.query2;

import ar.edu.itba.pod.Constants;
import ar.edu.itba.pod.data.Ticket;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.MultiMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.io.Serial;

public class Top3InfractionsByCityMapper implements Mapper<String, Ticket, String, String>, HazelcastInstanceAware {
    @Serial
    private static final long serialVersionUID = 1L; // Add a unique serialVersionUID

    private transient MultiMap<String, Ticket> tickets;

    public Top3InfractionsByCityMapper() {
    }


    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.tickets = hazelcastInstance.getMultiMap(Constants.HAZELCAST_NAMESPACE);
    }


    @Override
    public void map(String city, Ticket ticket, Context<String, String> context) {
        context.emit(ticket.getCountyName(), ticket.getInfractionCode());
        //this should be city, first arg?
    }
}