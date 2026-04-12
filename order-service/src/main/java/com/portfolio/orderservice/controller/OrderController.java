package com.portfolio.orderservice.controller;

import com.portfolio.orderservice.dto.CreateOrderRequest;
import com.portfolio.orderservice.model.CustomerOrder;
import com.portfolio.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("service", "order-service", "status", "UP");
    }

    @PostMapping("/orders")
    public ResponseEntity<CustomerOrder> createOrder(@RequestBody CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                orderService.createOrder(request.userId(), request.isbn(), request.quantity(), request.canWait()));
    }

    @GetMapping("/orders/{id}")
    public CustomerOrder getOrder(@PathVariable String id) {
        return orderService.getOrder(id);
    }
}
