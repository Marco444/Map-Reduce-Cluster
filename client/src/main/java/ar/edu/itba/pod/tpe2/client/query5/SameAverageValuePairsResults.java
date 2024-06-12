package ar.edu.itba.pod.tpe2.client.query5;

import ar.edu.itba.pod.Util;
import ar.edu.itba.pod.tpe2.client.Result;

public class SameAverageValuePairsResults implements Result, Comparable<SameAverageValuePairsResults> {

    private final String group;
    private final String typeA;
    private final String typeB;

    public SameAverageValuePairsResults(String group, String typeA, String typeB) {
        this.group = group;
        this.typeA = typeA;
        this.typeB = typeB;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(group).append(Util.CSV_DELIMITER).append(typeA).append(Util.CSV_DELIMITER).append(typeB);
        return sb.toString();
    }

    @Override
    public int compareTo(SameAverageValuePairsResults sameAverageValuePairsResults) {
        int cmp = (-1) * group.compareTo(sameAverageValuePairsResults.group);
        if(cmp != 0) return cmp;
        cmp = typeA.compareTo(sameAverageValuePairsResults.typeA);
        if(cmp != 0) return cmp;
        return typeB.compareTo(sameAverageValuePairsResults.typeB);
    }
}
