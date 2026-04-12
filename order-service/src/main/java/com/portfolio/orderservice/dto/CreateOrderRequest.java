package com.portfolio.orderservice.dto;

/**
 * Request body for creating a new order.
 */
public record CreateOrderRequest(
        Long userId,
        String isbn,
        int quantity
) {
}

