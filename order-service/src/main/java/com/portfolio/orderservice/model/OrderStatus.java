package com.portfolio.orderservice.model;

/**
 * Order lifecycle states as defined in Docs/system-logic.md:
 *   PENDING_INVENTORY → RESERVED → COMPLETED
 *                          ↘ REJECTED
 */
public enum OrderStatus {
    PENDING_INVENTORY,
    RESERVED,
    COMPLETED,
    REJECTED
}

