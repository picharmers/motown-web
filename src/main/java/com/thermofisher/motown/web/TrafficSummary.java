package com.thermofisher.motown.web;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Formatter;

public class TrafficSummary {

    private final long datehour;
    private final int trafficCount;

    public TrafficSummary(long datehour, int trafficCount) {
        this.datehour = datehour;
        this.trafficCount = trafficCount;
    }

    public long getDatehour() {
        return datehour;
    }

    public int getTrafficCount() {
        return trafficCount;
    }

    public String asJson() {
        return String.format("[ %d, %d ]", datehour, trafficCount);
    }

}
