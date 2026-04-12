package com.portfolio.inventoryservice.service;

import com.portfolio.inventoryservice.dto.FulfillmentRequest;
import com.portfolio.inventoryservice.dto.OrderEvent;
import com.portfolio.inventoryservice.kafka.OrderEventPublisher;
import com.portfolio.inventoryservice.model.Book;
import com.portfolio.inventoryservice.model.Reservation;
import com.portfolio.inventoryservice.repository.BookRepository;
import com.portfolio.inventoryservice.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FulfillmentService {

    private static final Logger log = LoggerFactory.getLogger(FulfillmentService.class);

    private final BookRepository bookRepository;
    private final ReservationRepository reservationRepository;
    private final OrderEventPublisher publisher;

    public FulfillmentService(BookRepository bookRepository,
                              ReservationRepository reservationRepository,
                              OrderEventPublisher publisher) {
        this.bookRepository = bookRepository;
        this.reservationRepository = reservationRepository;
        this.publisher = publisher;
    }

    @Transactional
    public String process(FulfillmentRequest request) {
        log.info("Processing fulfillment: orderId={} isbn={} quantity={} canWait={}",
                request.orderId(), request.isbn(), request.quantity(), request.canWait());

        Book book = bookRepository.findByIsbn(request.isbn()).orElse(null);

        String eventType;
        String reason;

        if (book == null) {
            eventType = "REJECTED";
            reason = "Book not found: " + request.isbn();
            log.warn("Book not found: isbn={}", request.isbn());
        } else if (book.getAvailableCopies() >= request.quantity()) {
            book.deduct(request.quantity());
            bookRepository.save(book);
            eventType = "COMPLETED";
            reason = "Stock available — fulfilled immediately";
            log.info("Copies deducted: isbn={} remaining={}", request.isbn(), book.getAvailableCopies());
        } else if (request.canWait()) {
            reservationRepository.save(new Reservation(request.orderId(), request.isbn(), request.quantity()));
            eventType = "RESERVED";
            reason = "Insufficient copies — order queued for reservation";
            log.info("Reservation created: orderId={} isbn={}", request.orderId(), request.isbn());
        } else {
            eventType = "REJECTED";
            reason = "Insufficient copies and waiting not allowed";
            log.info("Order rejected: orderId={} isbn={} available={} requested={}",
                    request.orderId(), request.isbn(), book.getAvailableCopies(), request.quantity());
        }

        publisher.publish(new OrderEvent(request.orderId(), eventType, reason, System.currentTimeMillis()));
        return eventType;
    }
}