package com.portfolio.orderservice.dto;

/**
 * Request body for creating a new order.
 * canWait — if true, the order will be reserved when stock is insufficient.
 */
public record CreateOrderRequest(
        Long userId,
        String isbn,
        int quantity,
        boolean canWait
) {
}

