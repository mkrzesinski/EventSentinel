package com.portfolio.inventoryservice;

import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@GetMapping("/health")
	public Map<String, String> health() {
		return Map.of(
				"service", "inventory-service",
				"status", "UP"
		);
	}
}
