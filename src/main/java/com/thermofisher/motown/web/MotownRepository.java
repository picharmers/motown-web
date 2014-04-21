package com.thermofisher.motown.web;

import java.util.List;

public interface MotownRepository {

    public List<TrafficEvent> getTrafficEventsForOneDevice(MotownParams params);

    public List<TrafficEvent> getTrafficEventsForAllDevices(MotownParams params);

}
