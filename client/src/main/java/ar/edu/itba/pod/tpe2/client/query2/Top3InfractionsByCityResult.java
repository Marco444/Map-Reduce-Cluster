package ar.edu.itba.pod.tpe2.client.query2;

import ar.edu.itba.pod.tpe2.client.Result;

public class Top3InfractionsByCityResult implements Result {
    private final String county;
    private final String top1Infraction;
    private final String top2Infraction;
    private final String top3Infraction;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(county).append(",").append(top1Infraction).append(",").append(top2Infraction).append(",").append(top3Infraction);
        return builder.toString();
    }

    public Top3InfractionsByCityResult(String county, String top1Infraction, String top2Infraction, String top3Infraction) {
        this.county = county;
        this.top1Infraction = top1Infraction;
        this.top2Infraction = top2Infraction;
        this.top3Infraction = top3Infraction;
    }

    public String getTop3Infraction() {
        return top3Infraction;
    }

    public String getTop2Infraction() {
        return top2Infraction;
    }

    public String getTop1Infraction() {
        return top1Infraction;
    }

    public String getCounty() {
        return county;
    }
}
