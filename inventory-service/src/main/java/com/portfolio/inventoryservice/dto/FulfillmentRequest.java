package com.portfolio.inventoryservice.dto;

/**
 * Request body accepted by the fulfillment endpoint.
 * See Docs/system-logic.md: OrderService → InventoryService (orderId, items, canWait).
 */
public record FulfillmentRequest(
        String orderId,
        String isbn,
        int quantity,
        boolean canWait
) {
}
