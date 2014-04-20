package com.thermofisher.motown.web;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet(name = "Motown", value = "/motown/*")
public class MotownServlet extends HttpServlet {

    private final DynamoDBMapper mapper;

    public MotownServlet() {
        AmazonDynamoDB db = new AmazonDynamoDBClient();
        mapper = new DynamoDBMapper(db);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String customerId = request.getParameter("customer_id");
        String deviceId = request.getParameter("device_id");
        String callback = request.getParameter("callback");
        int timespanInDays = Integer.parseInt(request.getParameter("timespan"));

        if (callback == null) {
            callback = "callback";
        }

        Calendar timespan = Calendar.getInstance();
        timespan.roll(Calendar.DAY_OF_MONTH, -timespanInDays);

        List<TrafficEvent> events = null;
        if (deviceId.equalsIgnoreCase("all")) {
            events = getTrafficEventsForAllDevices(customerId, timespan);
        } else {
            events = getTrafficEventsForOneDevice(customerId, deviceId, timespan);
        }

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
            trafficData.add(new TrafficSummary(Long.valueOf(dateHour), trafficEvents.size()));
        }

        setupResponse(response);

        PrintWriter writer = response.getWriter();
        writer.println(callback + "( [");
        for (Iterator<TrafficSummary> itr = trafficData.iterator(); itr.hasNext();) {
            TrafficSummary summary = itr.next();
            writer.println(summary.asJson());
            if (itr.hasNext()) {
                writer.println(", ");
            }
        }
        writer.println("] )");
    }

    private void setupResponse(HttpServletResponse response) {
        response.setContentType("application/json");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, OPTIONS, DELETE");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");
        response.addHeader("Access-Control-Max-Age", "86400");
    }

    private List<CustomerDevice> getCustomerDevices(String customerId) {
        DynamoDBQueryExpression<CustomerDevice> expression = new DynamoDBQueryExpression<CustomerDevice>()
                .withHashKeyValues(new CustomerDevice().withCustomerId(customerId));
        return mapper.query(CustomerDevice.class, expression);
    }

    private List<TrafficEvent> getTrafficEventsForOneDevice(String customerId, String deviceId, Calendar timespan) {
        String customerDevice = customerId + "::" + deviceId;

        Condition condition = new Condition()
                .withComparisonOperator(ComparisonOperator.GT.toString())
                .withAttributeValueList(new AttributeValue().withS(String.valueOf(timespan.getTimeInMillis())));

        DynamoDBQueryExpression<TrafficEvent> expression = new DynamoDBQueryExpression<TrafficEvent>()
                .withHashKeyValues(new TrafficEvent().withCustomerDevice(customerDevice))
                .withRangeKeyCondition("timestamp", condition);

        return mapper.query(TrafficEvent.class, expression);
    }

    private List<TrafficEvent> getTrafficEventsForAllDevices(String customerId, Calendar timespan) {
        List<CustomerDevice> customerDevices = getCustomerDevices(customerId);
        List<TrafficEvent> allDeviceEvents = new ArrayList<>();
        for (CustomerDevice customerDevice : customerDevices) {
            allDeviceEvents.addAll(getTrafficEventsForOneDevice(customerId, customerDevice.getDeviceId(), timespan));
        }
        return allDeviceEvents;
    }

}
