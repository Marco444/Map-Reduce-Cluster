package ar.edu.itba.pod.tpe2.client.query1;

import ar.edu.itba.pod.Constants;
import ar.edu.itba.pod.tpe2.client.Result;

public class TotalFinesByInfractionsResult implements Result {
    private final String infraction;
    private final Integer tickets;

    public TotalFinesByInfractionsResult(String infraction, Integer tickets) {
        this.infraction = infraction;
        this.tickets = tickets;
    }

    @Override
    public String toString() {
        return infraction + Constants.CSV_DELIMITER + tickets;
    }

    public String getInfraction() {
        return infraction;
    }

    public Integer getTickets() {
        return tickets;
    }
}
