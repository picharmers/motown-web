package com.thermofisher.motown.web;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TrafficEventTest {

    private TrafficEvent event;

    @Before
    public void setup() throws Exception {
        event = new TrafficEvent();
    }
    
    @After
    public void tearDown() throws Exception {
        event = null;
    }
    
    @Test
    public void testGetCustomerDevice() throws Exception {
        event.setCustomerDevice("customer::device");
        assertThat(event.getCustomerDevice(), is("customer::device"));
    }

    @Test
    public void testWithCustomerDevice() throws Exception {
        TrafficEvent actualEvent = event.withCustomerDevice("customer::device");
        assertThat(actualEvent.getCustomerDevice(), is("customer::device"));
    }

    @Test
    public void testGetCustomerId() throws Exception {
        event.setCustomerDevice("customer::device");
        assertThat(event.getCustomerId(), is("customer"));
    }

    @Test
    public void testGetDeviceId() throws Exception {
        event.setCustomerDevice("customer::device");
        assertThat(event.getDeviceId(), is("device"));
    }

    @Test
    public void testGetTimestamp() throws Exception {
        event.setTimestamp("someTimestamp");
        assertThat(event.getTimestamp(), is("someTimestamp"));
    }

    @Test
    public void testGetDateHour() throws Exception {
        event.setTimestamp("123456789");
        assertThat(event.getDateHour(), is("123454800000"));
    }
}
