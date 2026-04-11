package com.portfolio.inventoryservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "stock")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String productId;

    @Column(nullable = false)
    private int available;

    protected Stock() {}

    public Stock(String productId, int available) {
        this.productId = productId;
        this.available = available;
    }

    public Long getId() { return id; }
    public String getProductId() { return productId; }
    public int getAvailable() { return available; }

    public void deduct(int quantity) {
        this.available -= quantity;
    }
}