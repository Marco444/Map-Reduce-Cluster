package ar.edu.itba.pod.query4;

import ar.edu.itba.pod.Util;
import ar.edu.itba.pod.data.Infractions;
import ar.edu.itba.pod.data.Ticket;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.MultiMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.io.Serial;
import java.time.LocalDate;

public class MostInfractionsInRangeMapper implements Mapper<String, Ticket, String, String> {

    @Serial
    private static final long serialVersionUID = 1L; // Add a unique serialVersionUID

    private final LocalDate from;
    private final LocalDate to;

    public MostInfractionsInRangeMapper(LocalDate from, LocalDate to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public void map(String s, Ticket ticket, Context<String, String> context) {
        if (ticket.getIssueDate().isBefore(from) || ticket.getIssueDate().isAfter(to)) {
            return;
        }
        context.emit(ticket.getCountyName(), ticket.getLicensePlate());
    }
}
