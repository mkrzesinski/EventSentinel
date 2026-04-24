package com.portfolio.inventoryservice;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("h2")
@EmbeddedKafka(
        partitions = 3,
        topics = "order-events",
        bootstrapServersProperty = "spring.kafka.bootstrap-servers"
)
class HealthCheckH2Test extends AbstractHealthCheckTest {
}
