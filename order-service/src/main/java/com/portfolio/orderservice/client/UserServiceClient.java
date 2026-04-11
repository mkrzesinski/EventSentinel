package com.portfolio.orderservice.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
public class UserServiceClient {

    private static final Logger log = LoggerFactory.getLogger(UserServiceClient.class);

    private final RestClient restClient;

    public UserServiceClient(@Value("${user-service.url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public boolean validateUser(Long userId) {
        try {
            restClient.get()
                    .uri("/users/{id}/validate", userId)
                    .retrieve()
                    .toBodilessEntity();
            log.debug("User validation succeeded: userId={}", userId);
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("User not found during validation: userId={}", userId);
            return false;
        }
    }
}