package com.thermofisher.motown.web;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;

import java.util.ArrayList;
import java.util.List;

public class MotownRepositoryImpl implements MotownRepository {

    private static final MotownRepositoryImpl SINGLETON = new MotownRepositoryImpl();

    public static MotownRepository instance() {
        return SINGLETON;
    }

    private final DynamoDBMapper mapper;

    private MotownRepositoryImpl() {
        AmazonDynamoDB db = new AmazonDynamoDBClient();
        mapper = new DynamoDBMapper(db);
    }

    @Override
    public List<TrafficEvent> getTrafficEventsForOneDevice(MotownParams params) {
        Condition condition = new Condition()
                .withComparisonOperator(ComparisonOperator.GT.toString())
                .withAttributeValueList(new AttributeValue().withS(params.getSince()));

        DynamoDBQueryExpression<TrafficEvent> expression = new DynamoDBQueryExpression<TrafficEvent>()
                .withHashKeyValues(new TrafficEvent().withCustomerDevice(params.getCustomerDevice()))
                .withRangeKeyCondition("timestamp", condition);

        return mapper.query(TrafficEvent.class, expression);
    }

    @Override
    public List<TrafficEvent> getTrafficEventsForAllDevices(MotownParams params) {
        List<CustomerDevice> customerDevices = getCustomerDevices(params.getCustomerId());
        List<TrafficEvent> allDeviceEvents = new ArrayList<>();
        for (CustomerDevice customerDevice : customerDevices) {
            allDeviceEvents.addAll(getTrafficEventsForOneDevice(params));
        }
        return allDeviceEvents;
    }

    private List<CustomerDevice> getCustomerDevices(String customerId) {
        DynamoDBQueryExpression<CustomerDevice> expression = new DynamoDBQueryExpression<CustomerDevice>()
                .withHashKeyValues(new CustomerDevice().withCustomerId(customerId));
        return mapper.query(CustomerDevice.class, expression);
    }

}
