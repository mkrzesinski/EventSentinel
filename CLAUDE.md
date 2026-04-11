# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project State

This is a **runtime skeleton guided by docs**, not a finished system. Architecture is fully described in `README.md`, `system-logic.md`, `infrastructure.md`, and `Docs/*.puml`; code contains minimal Spring Boot skeletons with no business logic, entities, repositories, service clients, or Kafka producers/consumers yet implemented.

Read before coding: `README.md`, `system-logic.md`, `infrastructure.md`, `project-roadmap.md`, `Docs/flow.uml`, `Docs/infrastructure_diagram.puml`.

## Commands

```bash
# Build & test
mvn -q validate
mvn -q test
mvn -q package -DskipTests

# Docker
docker compose config
docker compose build
docker compose up

# Run services locally (after package)
java -jar user-service/target/user-service-0.0.1-SNAPSHOT.jar --server.port=18081
java -jar order-service/target/order-service-0.0.1-SNAPSHOT.jar --server.port=18082
java -jar inventory-service/target/inventory-service-0.0.1-SNAPSHOT.jar --server.port=18083

# Health checks (Docker Compose ports)
curl http://127.0.0.1:8081/health
curl http://127.0.0.1:8082/health
curl http://127.0.0.1:8083/health
```

## Architecture

**Three Spring Boot 3.3.2 services (Java 23), Maven multi-module, Docker Compose + Kafka.**

| Service | Port (Compose) | Package | Role |
|---|---|---|---|
| user-service | 8081 | `com.portfolio.userservice` | User authority / validation |
| order-service | 8082 | `com.portfolio.orderservice` | Owns order state and lifecycle |
| inventory-service | 8083 | `com.portfolio.inventoryservice` | Owns fulfillment decisions |

**Communication model** (intended, not yet implemented):
- **REST** (sync): Order Service → User Service (validate), Order Service → Inventory Service (submit)
- **Kafka** (async): Inventory Service publishes order lifecycle events → Order Service consumes them

**Order state machine**: `PENDING_INVENTORY` → `RESERVED` → `COMPLETED` or `REJECTED`

**Kafka partitioning**: events are keyed by `orderId` — preserve this when adding producers/consumers.

**Docker Compose** currently wires three HTTP services + Kafka (port 29092) + Zookeeper (port 2181). PostgreSQL and all business endpoints are not yet wired.

**Planned but not yet in code**: PostgreSQL (per service), H2 (tests), TestNG + REST Assured, Splunk, AI orchestrator (OpenAI GPT for test generation/log analysis).

## Conventions

- All services bind to `server.port: 8080` internally; Compose maps them to 8081/8082/8083.
- Keep changes **module-local** unless changing cross-service contracts.
- Follow the established package split: `com.portfolio.userservice`, `com.portfolio.orderservice`, `com.portfolio.inventoryservice`.
- When implementing documented architecture, update the matching design docs if behavior diverges.
- Make "current state vs planned architecture" explicit in docs and PRs.
- If adding shared event schemas or client contracts, document ownership boundary clearly.