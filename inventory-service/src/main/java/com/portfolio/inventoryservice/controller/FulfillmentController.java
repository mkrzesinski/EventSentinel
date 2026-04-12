package com.portfolio.inventoryservice.controller;

import com.portfolio.inventoryservice.dto.FulfillmentRequest;
import com.portfolio.inventoryservice.service.FulfillmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class FulfillmentController {

    private final FulfillmentService fulfillmentService;

    public FulfillmentController(FulfillmentService fulfillmentService) {
        this.fulfillmentService = fulfillmentService;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("service", "inventory-service", "status", "UP");
    }

    @PostMapping("/fulfillment")
    public ResponseEntity<Map<String, String>> submitOrder(@RequestBody FulfillmentRequest request) {
        String decision = fulfillmentService.process(request);
        return ResponseEntity.accepted().body(Map.of(
                "orderId", request.orderId(),
                "decision", decision
        ));
    }
}
