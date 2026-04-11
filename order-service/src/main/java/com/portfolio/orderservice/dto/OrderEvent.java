package com.portfolio.orderservice.dto;

/**
 * Event consumed by order-service from the "order-events" Kafka topic.
 * Keyed by orderId to guarantee ordering within a single order lifecycle.
 *
 * NOTE: The same schema is duplicated in inventory-service (producer side).
 * If a shared-contracts module is introduced later, move this record there.
 */
public record OrderEvent(
        String orderId,
        String eventType,   // COMPLETED | RESERVED | REJECTED
        String reason,
        long timestamp
) {
}

