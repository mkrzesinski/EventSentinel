package com.portfolio.inventoryservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.inventoryservice.config.KafkaTopicConfig;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import com.portfolio.inventoryservice.dto.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Publishes order lifecycle events to Kafka.
 * Messages are keyed by orderId to guarantee partition-level ordering
 * for a single order lifecycle (see Docs/infrastructure.md).
 */
@Component
public class OrderEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(OrderEventPublisher.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
            justification = "KafkaTemplate and ObjectMapper are thread-safe Spring-managed beans")
    public OrderEventPublisher(KafkaTemplate<String, String> kafkaTemplate,
                               ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(OrderEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(KafkaTopicConfig.ORDER_EVENTS_TOPIC, event.orderId(), payload);
            log.info("Published event [{}] for orderId={}", event.eventType(), event.orderId());
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize OrderEvent for orderId={}", event.orderId(), e);
        }
    }
}

