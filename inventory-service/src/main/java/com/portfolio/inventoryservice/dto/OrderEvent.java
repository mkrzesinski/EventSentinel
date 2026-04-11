package com.portfolio.inventoryservice.dto;

/**
 * Event published by inventory-service to the "order-events" Kafka topic.
 * Keyed by orderId to guarantee ordering within a single order lifecycle.
 *
 * NOTE: The same schema is duplicated in order-service (consumer side).
 * If a shared-contracts module is introduced later, move this record there.
 */
public record OrderEvent(
        String orderId,
        String eventType,   // COMPLETED | RESERVED | REJECTED
        String reason,
        long timestamp
) {
}

