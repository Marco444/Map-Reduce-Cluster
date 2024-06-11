package ar.edu.itba.pod.tpe2.client.query3;

import ar.edu.itba.pod.Util;
import ar.edu.itba.pod.tpe2.client.Result;

public class TopNEarningAgenciesResult implements Result, Comparable<TopNEarningAgenciesResult> {

    private final String agency;
    private final double percentage;

    public TopNEarningAgenciesResult(String agency, double percentage) {
        this.agency = agency;
        this.percentage = percentage;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(agency).append(Util.CSV_DELIMITER).append(percentage).append("%");
        return sb.toString();
    }

    @Override
    public int compareTo(TopNEarningAgenciesResult topNEarningAgenciesResult) {
        int cmp = Double.compare(this.percentage, topNEarningAgenciesResult.percentage);
        if (cmp == 0) {
            return (-1) * this.agency.compareTo(topNEarningAgenciesResult.agency);
        }
        return (-1) * cmp;
    }
}
