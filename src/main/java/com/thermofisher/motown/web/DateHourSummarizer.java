package com.thermofisher.motown.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DateHourSummarizer implements TrafficSummarizer {

    @Override
    public List<TrafficSummary> getTrafficData(MotownParams params, List<TrafficEvent> events) {
        List<TrafficSummary> trafficData = new ArrayList<>();
        Map<String, List<TrafficEvent>> eventMap = new HashMap<>();
        for (TrafficEvent event : events) {
            List<TrafficEvent> hourEvents = eventMap.get(event.getDateHour());
            if (hourEvents != null) {
                hourEvents.add(event);
            } else {
                hourEvents = new ArrayList<>();
                hourEvents.add(event);
                eventMap.put(event.getDateHour(), hourEvents);
            }
        }

        for (String dateHour : eventMap.keySet()) {
            List<TrafficEvent> trafficEvents = eventMap.get(dateHour);
            trafficData.add(new TrafficDateHourSummary(Long.valueOf(dateHour), trafficEvents.size()));
        }
        return trafficData;

    }
}
