# Project Roadmap — AI-Driven Test Orchestrator

## 1. Foundation

**Goal:** Establish project direction and documentation.

Scope:
1. Finalize main README
2. Add `system-logic.md`
3. Add `infrastructure.md`
4. Prepare UML diagrams
5. Define service responsibilities and architecture decisions

**Outcome:**
- Clear and consistent project vision
- Documented architecture and workflows

---

## 2. Runtime Skeleton

**Goal:** Run a minimal but working environment.

Scope:
1. Create repository structure
2. Prepare base `docker-compose.yml`
3. Add containers:
   - user-service
   - order-service
   - inventory-service
   - Kafka
   - Zookeeper
   - PostgreSQL instances
4. Implement basic health endpoints

**Outcome:**
- System starts with `docker-compose up`
- Services are reachable and connected

---

## 3. User Service MVP

**Goal:** Implement first business service.

Scope:
1. Create user
2. Retrieve / validate user
3. Persist data in database
4. Basic logging

**Outcome:**
- Functional user-service
- Available for validation by other services

---

## 4. Order Service MVP

**Goal:** Introduce main entry point for orders.

Scope:
1. Accept order requests
2. Validate user via User Service
3. Persist order in database
4. Assign initial status: `PENDING_INVENTORY`

**Outcome:**
- System can accept and store orders

---

## 5. Inventory Service MVP

**Goal:** Implement fulfillment logic.

Scope:
1. Accept fulfillment requests
2. Check stock availability
3. Decide:
   - completed
   - reserved
   - rejected
4. Persist reservation queue if needed

**Outcome:**
- Inventory becomes the owner of fulfillment logic

---

## 6. End-to-End Flow (REST-based)

**Goal:** Achieve full synchronous flow.

Scope:
1. Order → Inventory via REST
2. Inventory processes request
3. Handle all decision paths

**Outcome:**
- Fully working business flow without events

---

## 7. Kafka Integration

**Goal:** Introduce asynchronous communication.

Scope:
1. Inventory publishes events
2. Order consumes events
3. Update order status based on events
4. Define topics and event types

**Outcome:**
- Event-driven communication enabled
- Decoupled services

---

## 8. Data Model Stabilization

**Goal:** Improve persistence layer.

Scope:
1. Refine database schemas
2. Formalize order lifecycle states
3. Prepare for database migrations
4. Ensure consistency between services

**Outcome:**
- Stable and scalable data model

---

## 9. Traffic Generator

**Goal:** Simulate external client behavior.

Scope:
1. Generate orders automatically
2. Configure traffic patterns
3. Support different scenarios
4. Integrate with services via REST

**Outcome:**
- System receives controlled, realistic traffic

---

## 10. Test Orchestrator MVP

**Goal:** Introduce automated validation layer.

Scope:
1. Subscribe to Kafka events
2. Validate order lifecycle
3. Correlate requests with outcomes
4. Generate basic reports

**Outcome:**
- Automated E2E validation in place

---

## 11. Observability Layer

**Goal:** Gain visibility into system behavior.

Scope:
1. Centralize logs (Splunk)
2. Correlate logs with events
3. Detect anomalies

**Outcome:**
- Improved debugging and monitoring capabilities

---

## 12. AI Integration

**Goal:** Add intelligent test capabilities.

Scope:
1. Generate test payloads
2. Generate scenarios
3. Analyze results
4. Support anomaly detection

**Outcome:**
- AI-enhanced testing system

---

## 13. Reliability & Hardening

**Goal:** Prepare for real-world scenarios.

Scope:
1. Retry mechanisms
2. Idempotency
3. Duplicate event handling
4. Error handling
5. Edge case coverage

**Outcome:**
- Robust and production-like system behavior

---

## 14. Finalization & Presentation

**Goal:** Prepare project for showcasing.

Scope:
1. Final documentation
2. Diagrams refinement
3. Demo scenarios
4. Architecture summary

**Outcome:**
- Portfolio-ready project

---

## Simplified Execution Order

1. Documentation
2. Runtime setup
3. User Service
4. Order Service
5. Inventory Service
6. End-to-end flow
7. Kafka
8. Traffic Generator
9. Test Orchestrator
10. Observability
11. AI
12. Hardening
13. Final polish
