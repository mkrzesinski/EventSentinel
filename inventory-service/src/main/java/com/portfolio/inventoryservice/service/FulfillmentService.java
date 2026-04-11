package com.portfolio.inventoryservice.service;

import com.portfolio.inventoryservice.dto.FulfillmentRequest;
import com.portfolio.inventoryservice.dto.OrderEvent;
import com.portfolio.inventoryservice.kafka.OrderEventPublisher;
import com.portfolio.inventoryservice.model.Reservation;
import com.portfolio.inventoryservice.model.Stock;
import com.portfolio.inventoryservice.repository.ReservationRepository;
import com.portfolio.inventoryservice.repository.StockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FulfillmentService {

    private static final Logger log = LoggerFactory.getLogger(FulfillmentService.class);

    private final StockRepository stockRepository;
    private final ReservationRepository reservationRepository;
    private final OrderEventPublisher publisher;

    public FulfillmentService(StockRepository stockRepository,
                              ReservationRepository reservationRepository,
                              OrderEventPublisher publisher) {
        this.stockRepository = stockRepository;
        this.reservationRepository = reservationRepository;
        this.publisher = publisher;
    }

    @Transactional
    public String process(FulfillmentRequest request) {
        log.info("Processing fulfillment: orderId={} productId={} quantity={} canWait={}",
                request.orderId(), request.productId(), request.quantity(), request.canWait());

        Stock stock = stockRepository.findByProductId(request.productId()).orElse(null);

        String eventType;
        String reason;

        if (stock == null) {
            eventType = "REJECTED";
            reason = "Product not found: " + request.productId();
            log.warn("Product not found: productId={}", request.productId());
        } else if (stock.getAvailable() >= request.quantity()) {
            stock.deduct(request.quantity());
            stockRepository.save(stock);
            eventType = "COMPLETED";
            reason = "Stock available — fulfilled immediately";
            log.info("Stock deducted: productId={} remaining={}", request.productId(), stock.getAvailable());
        } else if (request.canWait()) {
            reservationRepository.save(new Reservation(request.orderId(), request.productId(), request.quantity()));
            eventType = "RESERVED";
            reason = "Insufficient stock — order queued for reservation";
            log.info("Reservation created: orderId={} productId={}", request.orderId(), request.productId());
        } else {
            eventType = "REJECTED";
            reason = "Insufficient stock and waiting not allowed";
            log.info("Order rejected: orderId={} productId={} available={} requested={}",
                    request.orderId(), request.productId(), stock.getAvailable(), request.quantity());
        }

        publisher.publish(new OrderEvent(request.orderId(), eventType, reason, System.currentTimeMillis()));
        return eventType;
    }
}