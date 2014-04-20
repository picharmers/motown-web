package com.thermofisher.motown.web;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "MOTOWN_CUSTOMER_DEVICE")
public class CustomerDevice {

    private String customerId;
    private String deviceId;

    public CustomerDevice() {
    }

    @DynamoDBHashKey(attributeName="customer_id")
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @DynamoDBRangeKey(attributeName="device_id")
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public CustomerDevice withCustomerId(String customerId) {
        this.customerId = customerId;
        return this;
    }
}
