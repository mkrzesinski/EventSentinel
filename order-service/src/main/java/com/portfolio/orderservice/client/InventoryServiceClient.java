package com.portfolio.orderservice.client;

import com.portfolio.orderservice.dto.FulfillmentRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class InventoryServiceClient {

    private static final Logger log = LoggerFactory.getLogger(InventoryServiceClient.class);

    private final RestClient restClient;

    public InventoryServiceClient(RestClient.Builder restClientBuilder,
                                  @Value("${inventory-service.url}") String baseUrl) {
        this.restClient = restClientBuilder.baseUrl(baseUrl).build();
    }

    /**
     * Submits an order for fulfillment. The actual outcome arrives asynchronously via Kafka.
     * A successful call means inventory has received and accepted the request.
     */
    public void submitFulfillment(String orderId, String isbn, int quantity, boolean canWait) {
        try {
            restClient.post()
                    .uri("/fulfillment")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new FulfillmentRequest(orderId, isbn, quantity, canWait))
                    .retrieve()
                    .toBodilessEntity();
            log.info("Fulfillment request submitted: orderId={} isbn={} quantity={}", orderId, isbn, quantity);
        } catch (Exception e) {
            log.error("Failed to submit fulfillment request: orderId={} — {}", orderId, e.getMessage());
        }
    }
}
