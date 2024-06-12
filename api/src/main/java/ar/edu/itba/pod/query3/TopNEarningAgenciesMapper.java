package ar.edu.itba.pod.query3;

import ar.edu.itba.pod.Constants;
import ar.edu.itba.pod.data.Ticket;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.MultiMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.io.Serial;

public class TopNEarningAgenciesMapper implements Mapper<String, Ticket, String, Double>, HazelcastInstanceAware{

    @Serial
    private static final long serialVersionUID = 1L; // Add a unique serialVersionUID

    private transient MultiMap<String, Ticket> tickets;

    public TopNEarningAgenciesMapper() {
    }


    @Override
    public void setHazelcastInstance (HazelcastInstance hazelcastInstance){
        this.tickets = hazelcastInstance.getMultiMap(Constants.HAZELCAST_NAMESPACE);
    }

    @Override
    public void map(String s, Ticket ticket, Context<String, Double> context) {
        context.emit(ticket.getIssuingAgency(), ticket.getFineAmount());
    }
}