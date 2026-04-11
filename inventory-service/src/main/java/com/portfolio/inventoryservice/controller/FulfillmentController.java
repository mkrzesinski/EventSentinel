package com.portfolio.inventoryservice.controller;

import com.portfolio.inventoryservice.dto.FulfillmentRequest;
import com.portfolio.inventoryservice.dto.OrderEvent;
import com.portfolio.inventoryservice.kafka.OrderEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Accepts fulfillment requests from order-service and publishes lifecycle
 * events to Kafka. This is a minimal stub — full stock checking and
 * reservation logic will be added in roadmap step 5 (Inventory Service MVP).
 *
 * Current simplified logic:
 *   quantity <= 100           → COMPLETED
 *   quantity > 100 && canWait → RESERVED
 *   otherwise                 → REJECTED
 */
@RestController
@RequestMapping("/fulfillment")
public class FulfillmentController {

    private static final Logger log = LoggerFactory.getLogger(FulfillmentController.class);

    private final OrderEventPublisher publisher;

    public FulfillmentController(OrderEventPublisher publisher) {
        this.publisher = publisher;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> submitOrder(@RequestBody FulfillmentRequest request) {
        log.info("Fulfillment request received for orderId={} quantity={} canWait={}",
                request.orderId(), request.quantity(), request.canWait());

        String eventType;
        String reason;

        if (request.quantity() <= 100) {
            eventType = "COMPLETED";
            reason = "Stock available — fulfilled immediately";
        } else if (request.canWait()) {
            eventType = "RESERVED";
            reason = "Insufficient stock — order queued for reservation";
        } else {
            eventType = "REJECTED";
            reason = "Insufficient stock and waiting not allowed";
        }

        OrderEvent event = new OrderEvent(
                request.orderId(),
                eventType,
                reason,
                System.currentTimeMillis()
        );

        publisher.publish(event);

        return ResponseEntity.accepted().body(Map.of(
                "orderId", request.orderId(),
                "decision", eventType
        ));
    }
}

