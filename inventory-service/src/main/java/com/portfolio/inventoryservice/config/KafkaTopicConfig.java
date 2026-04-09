package com.portfolio.inventoryservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Kafka topic definitions owned by inventory-service.
 * 3 partitions allow parallel consumption while orderId-keyed messages
 * guarantee ordering within a single order lifecycle.
 */
@Configuration
public class KafkaTopicConfig {

    public static final String ORDER_EVENTS_TOPIC = "order-events";

    @Bean
    public NewTopic orderEventsTopic() {
        return new NewTopic(ORDER_EVENTS_TOPIC, 3, (short) 1);
    }
}

