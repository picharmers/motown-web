package com.thermofisher.motown.web;

import java.util.List;

public interface TrafficSummarizer {

    public List<TrafficSummary> getTrafficData(MotownParams params, List<TrafficEvent> events);

}
