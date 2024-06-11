package ar.edu.itba.pod.tpe2.client.query4;

import ar.edu.itba.pod.Util;
import ar.edu.itba.pod.tpe2.client.Result;

public class MostInfractionsInRangeResult implements Result, Comparable<MostInfractionsInRangeResult> {
    private final String county;
    private final String plate;
    private final int tickets;


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(county).append(Util.CSV_DELIMITER).append(plate).append(Util.CSV_DELIMITER).append(tickets);
        return builder.toString();
    }

    public MostInfractionsInRangeResult(String county, String plate, int tickets) {
        this.county = county;
        this.plate = plate;
        this.tickets = tickets;
    }

    public String getPlate() {
        return plate;
    }

    public int getTickets() {
        return tickets;
    }

    public String getCounty() {
        return county;
    }

    @Override
    public int compareTo(MostInfractionsInRangeResult o) {
        int cmp = county.compareTo(o.getCounty());
        return cmp;
    }
}
