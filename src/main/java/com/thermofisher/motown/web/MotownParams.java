package com.thermofisher.motown.web;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

class MotownParams {

    private final String customerId;
    private final String deviceId;
    private final String callback;
    private final String since;
    private final TrafficSummarizer summarizer;

    public MotownParams(HttpServletRequest request) {
        customerId = request.getParameter("customer_id");
        deviceId = request.getParameter("device_id");
        callback = request.getParameter("callback");
        String path = request.getPathInfo();
        System.out.println("path info " + path);
        if (path.startsWith("/datehours")) {
            summarizer = new DateHourSummarizer();
        } else if (path.startsWith("/devices")) {
            summarizer = new DevicesSummarizer();
        } else {
            summarizer = null;
        }

        int timespanInDays = Integer.parseInt(request.getParameter("timespan"));
        Calendar timespan = Calendar.getInstance();
        timespan.roll(Calendar.DAY_OF_MONTH, -timespanInDays);
        since = String.valueOf(timespan.getTimeInMillis());
    }

    public TrafficSummarizer getSummarizer() {
        return summarizer;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCallback() {
        if (isEmpty(callback)) {
            return "callback";
        }
        return callback;
    }

    public String getSince() {
        return since;
    }

    public boolean isAllDevices() {
        return "all".equals(deviceId) || isEmpty(deviceId);
    }

    private boolean isEmpty(String value) {
        return value == null || "".equals(value);
    }

    public String getCustomerDevice() {
        return customerId + "::" + deviceId;
    }
}
