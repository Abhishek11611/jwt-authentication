package com.example.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface OrderService {

    Object orderSaved (Object data) throws JsonProcessingException;
}
