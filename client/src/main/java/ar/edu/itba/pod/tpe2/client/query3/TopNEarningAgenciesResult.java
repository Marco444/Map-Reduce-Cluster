package ar.edu.itba.pod.tpe2.client.query3;

import ar.edu.itba.pod.tpe2.client.Result;

public class TopNEarningAgenciesResult implements Result {

    private final String agency;
    private final double percentage;
    private final char DELIMITER = ',';

    public TopNEarningAgenciesResult(String agency, double percentage) {
        this.agency = agency;
        this.percentage = percentage;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(agency).append(DELIMITER).append(percentage).append("%");
        return sb.toString();
    }

}
