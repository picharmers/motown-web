package com.thermofisher.motown.web;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TrafficDateHourSummaryTest {

    private TrafficDateHourSummary summary;

    @Before
    public void setup() throws Exception {
        summary = new TrafficDateHourSummary(120L, 10);
    }

    @After
    public void tearDown() throws Exception {
        summary = null;
    }

    @Test
    public void testGetDatehour() throws Exception {
        assertThat(summary.getDatehour(), is(120L));
    }

    @Test
    public void testGetTrafficCount() throws Exception {
        assertThat(summary.getTrafficCount(), is(10));
    }

    @Test
    public void testAsJson() throws Exception {
        assertThat(summary.asJson(), is("[ 120, 10 ]"));
    }
}
