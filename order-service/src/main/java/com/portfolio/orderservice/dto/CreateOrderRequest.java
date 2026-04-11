package com.portfolio.orderservice.dto;

/**
 * Request body for creating a new order.
 */
public record CreateOrderRequest(
        String userId
) {
}

