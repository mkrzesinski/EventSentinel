package com.portfolio.orderservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.orderservice.dto.OrderEvent;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import com.portfolio.orderservice.model.CustomerOrder;
import com.portfolio.orderservice.model.OrderStatus;
import com.portfolio.orderservice.repository.OrderRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

/**
 * Listens to the "order-events" Kafka topic and updates order status accordingly.
 * See Docs/system-logic.md for the full order lifecycle:
 *   PENDING_INVENTORY → RESERVED → COMPLETED
 *                          ↘ REJECTED
 */
@Component
public class OrderEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);

    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
            justification = "ObjectMapper is thread-safe after Spring Boot autoconfiguration")
    public OrderEventConsumer(OrderRepository orderRepository, ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "order-events", groupId = "order-service")
    public void consume(ConsumerRecord<String, String> record) {
        OrderEvent event;
        try {
            event = objectMapper.readValue(record.value(), OrderEvent.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize event from partition={} offset={}",
                    record.partition(), record.offset(), e);
            return;
        }

        log.info("Consumed event [{}] for orderId={}", event.eventType(), event.orderId());

        Optional<CustomerOrder> optionalOrder = orderRepository.findById(event.orderId());
        if (optionalOrder.isEmpty()) {
            log.warn("Received event for unknown orderId={}, ignoring", event.orderId());
            return;
        }

        CustomerOrder order = optionalOrder.get();
        OrderStatus newStatus = switch (event.eventType()) {
            case "COMPLETED" -> OrderStatus.COMPLETED;
            case "RESERVED" -> OrderStatus.RESERVED;
            case "REJECTED" -> OrderStatus.REJECTED;
            default -> {
                log.warn("Unknown event type '{}' for orderId={}", event.eventType(), event.orderId());
                yield null;
            }
        };

        if (newStatus != null) {
            order.setStatus(newStatus);
            order.setUpdatedAt(Instant.now());
            orderRepository.save(order);
            log.info("Order {} status updated to {}", event.orderId(), newStatus);
        }
    }
}

