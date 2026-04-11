package com.portfolio.orderservice.repository;

import com.portfolio.orderservice.model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<CustomerOrder, String> {
}

