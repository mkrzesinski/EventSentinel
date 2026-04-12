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

**Goal:** Improve persistence layer.

Scope:
- [ ] Refine database schemas
- [ ] Formalize order lifecycle states
- [ ] Prepare for database migrations
- [ ] Ensure consistency between services

**Outcome:**
- [ ] Stable and scalable data model

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
