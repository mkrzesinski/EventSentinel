package com.portfolio.orderservice.controller;

import com.portfolio.orderservice.dto.CreateOrderRequest;
import com.portfolio.orderservice.model.CustomerOrder;
import com.portfolio.orderservice.model.OrderStatus;
import com.portfolio.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST API for order management.
 * Created orders start in PENDING_INVENTORY status and are updated
 * asynchronously via Kafka events consumed by OrderEventConsumer.
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @PostMapping
    public ResponseEntity<CustomerOrder> createOrder(@RequestBody CreateOrderRequest request) {
        String orderId = UUID.randomUUID().toString();
        CustomerOrder order = new CustomerOrder(orderId, request.userId(), OrderStatus.PENDING_INVENTORY);
        orderRepository.save(order);
        log.info("Created order id={} userId={} status={}", orderId, request.userId(), order.getStatus());
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerOrder> getOrder(@PathVariable String id) {
        return orderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<CustomerOrder> listOrders() {
        return orderRepository.findAll();
    }
}

