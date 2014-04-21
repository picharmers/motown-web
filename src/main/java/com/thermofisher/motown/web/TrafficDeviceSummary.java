package com.thermofisher.motown.web;

public class TrafficDeviceSummary implements TrafficSummary {

    private final String deviceId;
    private final int trafficCount;

    public TrafficDeviceSummary(String deviceId, int trafficCount) {
        this.deviceId = deviceId;
        this.trafficCount = trafficCount;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public int getTrafficCount() {
        return trafficCount;
    }

    public String asJson() {
        return String.format("[ %s, %d ]", deviceId, trafficCount);
    }
}
