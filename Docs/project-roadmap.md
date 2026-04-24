# Project Roadmap — AI-Driven Test Orchestrator

## 1. Foundation

**Goal:** Establish project direction and documentation.

Scope:
- [x] Finalize main README
- [x] Add `system-logic.md`
- [x] Add `infrastructure.md`
- [x] Prepare UML diagrams
- [x] Define service responsibilities and architecture decisions

**Outcome:**
- [x] Clear and consistent project vision
- [x] Documented architecture and workflows

---

## 2. Runtime Skeleton

**Goal:** Run a minimal but working environment.

Scope:
- [x] Create repository structure
- [x] Prepare base `docker-compose.yml`
- [x] Add containers:
  - [x] user-service
  - [x] order-service
  - [x] inventory-service
  - [x] Kafka (KRaft mode — no Zookeeper)
  - [x] PostgreSQL instances
- [x] Implement basic health endpoints

**Outcome:**
- [x] System starts with `docker-compose up`
- [x] Services are reachable and connected

---

## 3. User Service MVP

**Goal:** Implement first business service.

Scope:
- [x] Create user
- [x] Retrieve / validate user
- [x] Persist data in database
- [x] Basic logging

**Outcome:**
- [x] Functional user-service
- [x] Available for validation by other services

---

## 4. Order Service MVP

**Goal:** Introduce main entry point for orders.

Scope:
- [x] Accept order requests
- [x] Validate user via User Service
- [x] Persist order in database
- [x] Assign initial status: `PENDING_INVENTORY`
- [x] Consume Kafka events and update order status (`COMPLETED` / `RESERVED` / `REJECTED`)

**Outcome:**
- [x] System can accept, store, and react to order lifecycle events

---

## 5. Inventory Service MVP

**Goal:** Implement fulfillment logic.

Scope:
- [x] Accept fulfillment requests
- [x] Check stock availability (physical books, keyed by ISBN)
- [x] Decide:
  - [x] completed
  - [x] reserved
  - [x] rejected
- [x] Persist reservation queue if needed
- [x] Publish order lifecycle events to Kafka (`order-events` topic, keyed by `orderId`)

**Outcome:**
- [x] Inventory is the owner of fulfillment logic and event emission

---

## 6. End-to-End Flow

**Goal:** Close the full order lifecycle.

Scope:
- [x] Order Service calls Inventory Service via REST (`POST /fulfillment`)
- [x] Inventory processes request and publishes Kafka event
- [x] Order Service consumes event and updates status
- [x] Handle all decision paths (completed / reserved / rejected)

**Outcome:**
- [x] Full end-to-end flow working: REST entry → fulfillment → event → status update

---

## 7. Data Model Stabilization

**Goal:** Replace Hibernate auto-DDL with a controlled, versioned persistence layer that is consistent across all services and runnable end-to-end from a clean start.

---

### 7.1 Flyway — Versioned Migrations

**Goal:** Replace `ddl-auto: update` with explicit, versioned SQL migration scripts per service.

**Context:** Currently Hibernate creates and alters tables automatically on startup. This works for a first run but loses control over schema shape — no indexes, no precise column types, no reproducible history. Any column rename or type change in a JPA entity can silently corrupt or fail on an existing database.

Scope:
- [x] Add Flyway dependency to `user-service`, `order-service`, `inventory-service`
- [x] Write `V1__init.sql` per service — explicit `CREATE TABLE` with PostgreSQL-native types
- [x] Switch `spring.jpa.hibernate.ddl-auto` from `update` to `validate` in all `application.yml`
- [ ] Verify clean `docker compose up` runs all three migration scripts without error

**Outcome:**
- [x] Schema changes tracked, reproducible, and reviewable in git — `ddl-auto: update` gone

---

### 7.2 Schema Formalization

**Goal:** Make PostgreSQL the authoritative source of schema truth with precise types, indexes, and DB-level constraints.

**Context:** JPA annotations define constraints only at the ORM layer — the actual PostgreSQL columns get generic types (e.g. `TEXT` for ISBN instead of `VARCHAR(13)`, `TIMESTAMP` without timezone). There are no explicit indexes beyond primary keys. Adding them later on a live database requires a migration anyway, so this is the right moment.

Scope:
- [x] Precise column types: `VARCHAR(13)` for ISBN, `TIMESTAMPTZ` for all timestamps, `VARCHAR(255)` for order ID (UUID string)
- [x] DB-level CHECK constraint on `customer_orders.status` matching the four `OrderStatus` enum values
- [x] Indexes: `customer_orders(user_id)` (order lookup by user), `customer_orders(status)` (future polling by test orchestrator), `reservations(isbn)` (stock fulfillment lookups)
- [x] `NOT NULL` and `UNIQUE` constraints explicitly in SQL, not only in `@Column`

**Outcome:**
- [x] Schema is precise, performant, and self-documenting at the SQL level

---

### 7.3 Model Consistency Cleanup

**Goal:** Fix cross-service inconsistencies in how timestamps and lifecycle state are handled.

**Context:** Timestamps are inconsistent — `CustomerOrder` uses `java.time.Instant`, while `User` and `Reservation` use `java.time.LocalDateTime`. `LocalDateTime` has no timezone information and will produce wrong values when running in Docker containers with non-UTC locale. Additionally, `CustomerOrder.updatedAt` is currently set manually in `OrderService` and `OrderEventConsumer` — this is fragile and easy to forget. There is no `@PreUpdate` hook on the entity.

Scope:
- [x] Standardize all timestamp fields to `Instant` / `TIMESTAMPTZ` across all three services (`User.createdAt`, `Reservation.createdAt` → `Instant`)
- [x] Add `@PrePersist` / `@PreUpdate` lifecycle hooks to `CustomerOrder` (remove manual `Instant.now()` from service layer)
- [x] Confirm `Reservation` has no mutable state that needs `@PreUpdate`

**Outcome:**
- [x] Timezone-safe, uniform persistence model — no silent time bugs when running in Docker

---

### 7.4 Seed Data for Inventory

**Goal:** Make the system runnable end-to-end from a fresh `docker compose up` without any manual bootstrapping.

**Context:** The `books` table is created empty on every fresh start. The E2E flow requires at least one book with known ISBN to produce a `COMPLETED` decision. Without seed data, every new environment (CI, local reset, demo) requires a manual `POST /books` call before any order can be fulfilled. The seed should cover all three fulfillment decision paths so they can be tested deterministically.

Scope:
- [x] Write `V2__seed_books.sql` for `inventory-service` with at least three books:
  - One with sufficient stock → flow path: `COMPLETED`
  - One with zero stock → flow path: `REJECTED` (when `canWait=false`)
  - One with zero stock → flow path: `RESERVED` (when `canWait=true`)
- [x] Document the seed ISBNs in `system-logic.md` or inline in the migration file

**Outcome:**
- [x] Full E2E flow works immediately after `docker compose up` — no manual API calls needed

---

### 7.5 Dual Test Environment Setup

**Goal:** Introduce two independent, equivalent test environments — one lightweight (H2), one production-faithful (Testcontainers) — activated via Maven profiles.

**Context:** A single test suite runs against both environments. The goal is to detect divergences between H2 behavior and real PostgreSQL/Kafka — not to split tests by type, but to compare results across platforms. Both environments are fully independent of the running Docker Compose system.

#### Profile: `h2` (fast, Docker-free)
- H2 in-memory replaces PostgreSQL
- Embedded Kafka replaces the real broker
- No Docker required — runs entirely in JVM
- Flyway uses PostgreSQL compatibility mode (`MODE=PostgreSQL`) or separate H2 migration path

#### Profile: `integration` (production-faithful)
- Real PostgreSQL via Testcontainers (Docker required, managed automatically)
- Real Kafka via Testcontainers
- Identical schema, identical Flyway migrations as production

#### Invocation
```bash
mvn test -P h2           # fast, no Docker
mvn test -P integration  # full fidelity, Docker required
```

Scope:
- [x] Add Maven profiles `h2` and `integration` to parent `pom.xml`
- [x] Add Testcontainers dependencies (PostgreSQL + Kafka modules)
- [x] Add `application-h2.yml` and `application-integration.yml` per service
- [x] Configure Flyway for H2 compatibility (Flyway disabled in H2 profile; Hibernate `create-drop` used instead — H2 does not support `TIMESTAMPTZ`)
- [x] Add minimal health check test per service (smoke baseline for both profiles)
- [x] Add `ci-h2.yml` and `ci-integration.yml` GitHub Actions workflows
- [ ] Verify both profiles pass on clean checkout

**Outcome:**
- [x] Same test suite runs on both environments — divergences between H2 and PostgreSQL are visible and comparable
- [x] CI runs three independent pipelines: smoke (Docker Compose), H2 tests, Testcontainers tests

---

## 8. Traffic Generator

**Goal:** Simulate external client behavior.

Scope:
- [ ] Generate orders automatically
- [ ] Configure traffic patterns
- [ ] Support different scenarios
- [ ] Integrate with services via REST

**Outcome:**
- [ ] System receives controlled, realistic traffic

---

## 9. Test Orchestrator MVP

**Goal:** Introduce automated validation layer.

Scope:
- [ ] Subscribe to Kafka events
- [ ] Validate order lifecycle
- [ ] Correlate requests with outcomes
- [ ] Generate basic reports

**Outcome:**
- [ ] Automated E2E validation in place

---

## 10. Observability Layer

**Goal:** Gain visibility into system behavior.

Scope:
- [ ] Centralize logs (Splunk)
- [ ] Correlate logs with events
- [ ] Detect anomalies

**Outcome:**
- [ ] Improved debugging and monitoring capabilities

---

## 11. AI Integration

**Goal:** Add intelligent test capabilities.

Scope:
- [ ] Generate test payloads
- [ ] Generate scenarios
- [ ] Analyze results
- [ ] Support anomaly detection

**Outcome:**
- [ ] AI-enhanced testing system

---

## 12. Reliability & Hardening

**Goal:** Prepare for real-world scenarios.

Scope:
- [ ] Retry mechanisms
- [ ] Idempotency
- [ ] Duplicate event handling
- [ ] Error handling
- [ ] Edge case coverage

**Outcome:**
- [ ] Robust and production-like system behavior

---

## 13. Finalization & Presentation

**Goal:** Prepare project for showcasing.

Scope:
- [ ] Final documentation
- [ ] Diagrams refinement
- [ ] Demo scenarios
- [ ] Architecture summary

**Outcome:**
- [ ] Portfolio-ready project

---

## Simplified Execution Order

- [x] Documentation
- [x] Runtime setup
- [x] User Service
- [x] Order Service (+ Kafka consumer)
- [x] Inventory Service (+ Kafka publisher)
- [x] End-to-end flow
- [ ] Traffic Generator
- [ ] Test Orchestrator
- [ ] Observability
- [ ] AI
- [ ] Hardening
- [ ] Final polish
