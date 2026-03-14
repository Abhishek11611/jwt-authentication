package com.example.demo.implementation;

import com.example.demo.service.OrderService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public Object orderSaved(Object data) throws JsonProcessingException {


        ObjectMapper objectMapper = new ObjectMapper();
//        String scrapedJson = objectMapper.writeValueAsString(data);
//        String actualString = objectMapper.readValue(scrapedJson, String.class);

        try {
            JsonNode jsonNode = objectMapper.valueToTree(data);

            String name = jsonNode.get("name").asText();
            String std = jsonNode.get("std").asText();
            int age = jsonNode.get("age").asInt();
            Map<String, Object> userMap = new HashMap<>();

            userMap.put("name", name);
            userMap.put("std", std);
            userMap.put("age", age);
            JsonNode address = jsonNode.get("address");
            String city = address.get("city").asText();
            String station = address.get("station").asText();
            Map<String, Object> addressMap = new HashMap<>();

            addressMap.put("city", city);
            addressMap.put("station", station);
            userMap.put("address", addressMap);

            List<Object> interestMap = new ArrayList<>();

            JsonNode interest = jsonNode.get("interest");

            for (JsonNode interestCopy : interest) {
                Map<String, Object> interestInnerMap = new HashMap<>();

                String nameInner = interestCopy.get("name").asText();
                int valueInner = interestCopy.get("value").asInt();
                interestInnerMap.put("name", nameInner);
                interestInnerMap.put("value", valueInner);
                interestMap.add(interestInnerMap);
            }
            userMap.put("interest", interestMap);


            return userMap;

        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
