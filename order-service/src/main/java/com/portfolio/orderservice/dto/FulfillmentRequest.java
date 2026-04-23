package com.portfolio.orderservice.dto;

public record FulfillmentRequest(
        String orderId,
        String isbn,
        int quantity,
        boolean canWait
) {
}