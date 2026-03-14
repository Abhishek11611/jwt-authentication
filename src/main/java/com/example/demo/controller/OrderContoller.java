package com.example.demo.controller;

import com.example.demo.response.ResponseHandler;
import com.example.demo.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/order")
public class OrderContoller {

    private final OrderService orderService;

    public OrderContoller(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/saved")
    public ResponseHandler orderSaved(@RequestBody Object data){
        ResponseHandler responseHandler = new ResponseHandler();

        try {
            Object s = orderService.orderSaved(data);
            responseHandler.setData(s);
            responseHandler.setMessage("Success");
            responseHandler.setStatus(true);
        } catch (Exception e) {
            e.printStackTrace();
            responseHandler.setData(e.getMessage());
            responseHandler.setMessage("Failed");
            responseHandler.setStatus(false);
        }

        return responseHandler;
    }
}
