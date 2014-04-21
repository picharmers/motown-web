package com.thermofisher.motown.web;

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

    private MotownRepository repository = MotownRepositoryImpl.instance();

    public void setRepository(MotownRepository repository) {
        this.repository = repository;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MotownParams params = new MotownParams(request);
        List<TrafficEvent> events = getTrafficEvents(params);
        List<TrafficSummary> trafficData = params.getSummarizer().getTrafficData(params, events);

        setupResponse(response);

        PrintWriter writer = response.getWriter();
        writer.println(params.getCallback() + "( [");
        for (Iterator<TrafficSummary> itr = trafficData.iterator(); itr.hasNext();) {
            TrafficSummary summary = itr.next();
            writer.println(summary.asJson());
            if (itr.hasNext()) {
                writer.println(", ");
            }
        }
        writer.println("] )");
    }

    private List<TrafficEvent> getTrafficEvents(MotownParams params) {
        List<TrafficEvent> events = null;
        if (params.isAllDevices()) {
            events = repository.getTrafficEventsForAllDevices(params);
        } else {
            events = repository.getTrafficEventsForOneDevice(params);
        }
        return events;
    }

    private void setupResponse(HttpServletResponse response) {
        response.setContentType("application/json");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, OPTIONS, DELETE");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");
        response.addHeader("Access-Control-Max-Age", "86400");
    }

}
