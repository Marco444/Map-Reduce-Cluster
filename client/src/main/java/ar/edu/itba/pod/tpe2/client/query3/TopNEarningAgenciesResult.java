package ar.edu.itba.pod.tpe2.client.query3;

import ar.edu.itba.pod.Constants;
import ar.edu.itba.pod.tpe2.client.Result;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class TopNEarningAgenciesResult implements Result, Comparable<TopNEarningAgenciesResult> {

    private final String agency;
    private final double percentage;

    public TopNEarningAgenciesResult(String agency, double percentage) {
        this.agency = agency;
        this.percentage = percentage;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.DOWN);
        String truncatedPercentage = df.format(percentage);

        sb.append(agency).append(Constants.CSV_DELIMITER).append(truncatedPercentage).append("%");
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
