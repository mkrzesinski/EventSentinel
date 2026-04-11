package com.portfolio.orderservice.service;

import com.portfolio.orderservice.client.UserServiceClient;
import com.portfolio.orderservice.model.CustomerOrder;
import com.portfolio.orderservice.model.OrderStatus;
import com.portfolio.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final UserServiceClient userServiceClient;

    public OrderService(OrderRepository orderRepository, UserServiceClient userServiceClient) {
        this.orderRepository = orderRepository;
        this.userServiceClient = userServiceClient;
    }

    public CustomerOrder createOrder(Long userId) {
        if (!userServiceClient.validateUser(userId)) {
            log.warn("Order rejected — user does not exist: userId={}", userId);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User not found: " + userId);
        }

        String orderId = UUID.randomUUID().toString();
        CustomerOrder order = new CustomerOrder(orderId, userId, OrderStatus.PENDING_INVENTORY);
        orderRepository.save(order);
        log.info("Order created: id={} userId={} status={}", orderId, userId, order.getStatus());
        return order;
    }

    public CustomerOrder getOrder(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Order not found: id={}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found: " + id);
                });
    }
}