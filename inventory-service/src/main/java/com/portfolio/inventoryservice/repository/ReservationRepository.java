package com.portfolio.inventoryservice.repository;

import com.portfolio.inventoryservice.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}