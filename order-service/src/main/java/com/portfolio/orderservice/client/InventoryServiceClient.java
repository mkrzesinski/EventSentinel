package com.portfolio.orderservice.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class InventoryServiceClient {

    private static final Logger log = LoggerFactory.getLogger(InventoryServiceClient.class);

    private final RestClient restClient;

    public InventoryServiceClient(@Value("${inventory-service.url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    /**
     * Submits an order for fulfillment. The actual outcome arrives asynchronously via Kafka.
     * A successful call means inventory has received and accepted the request.
     */
    public void submitFulfillment(String orderId, String isbn, int quantity, boolean canWait) {
        record FulfillmentRequest(String orderId, String isbn, int quantity, boolean canWait) {}

        try {
            restClient.post()
                    .uri("/fulfillment")
                    .body(new FulfillmentRequest(orderId, isbn, quantity, canWait))
                    .retrieve()
                    .toBodilessEntity();
            log.info("Fulfillment request submitted: orderId={} isbn={} quantity={}", orderId, isbn, quantity);
        } catch (RestClientException e) {
            log.error("Failed to submit fulfillment request: orderId={} — {}", orderId, e.getMessage());
        }
    }
}