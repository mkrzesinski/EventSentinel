package com.portfolio.orderservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * Named CustomerOrder (not Order) to avoid SQL reserved-word conflicts.
 */
@Entity
@Table(name = "customer_orders")
public class CustomerOrder {

    @Id
    private String id;

    private Long userId;

    private String isbn;

    private int quantity;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Instant createdAt;
    private Instant updatedAt;

    protected CustomerOrder() {
    }

    public CustomerOrder(String id, Long userId, String isbn, int quantity, OrderStatus status) {
        this.id = id;
        this.userId = userId;
        this.isbn = isbn;
        this.quantity = quantity;
        this.status = status;
    }

    @PrePersist
    private void prePersist() {
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    private void preUpdate() {
        updatedAt = Instant.now();
    }

    public String getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getQuantity() {
        return quantity;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
