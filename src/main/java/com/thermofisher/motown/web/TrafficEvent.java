package com.thermofisher.motown.web;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Calendar;
import java.util.Date;

@DynamoDBTable(tableName = "MOTOWN_EVENT_LOG")
public class TrafficEvent {

    private String customerDevice;
    private String timestamp;

    public TrafficEvent() {
    }

    @DynamoDBHashKey(attributeName="customer_device_identifier")
    public String getCustomerDevice() {
        return customerDevice;
    }

    public void setCustomerDevice(String customerDevice) {
        this.customerDevice = customerDevice;
    }

    public TrafficEvent withCustomerDevice(String customerDevice) {
        this.customerDevice = customerDevice;
        return this;
    }

    public String getCustomerId() {
        return customerDevice.split("::")[0];
    }

    public String getDeviceId() {
        return customerDevice.split("::")[1];
    }

    /**
     * Timestamp is in seconds, and is typically a float expressed as a string in DDB.
     * @return
     */
    @DynamoDBRangeKey(attributeName="timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Return the timestamp in milliseconds of the hour in which the timestamp is.
     * @return
     */
    public String getDateHour() {
        long timeInSeconds = Float.valueOf(timestamp).longValue();
        long timeInHours = timeInSeconds / (60 * 60);
        return String.valueOf(timeInHours * 60 * 60 * 1000);
    }

}
