# 🧠 AI-Driven Test Orchestrator for Microservices

## 📌 Project Overview

This project demonstrates a modern, AI-assisted approach to automated testing in a microservice's architecture. It introduces a **Test Orchestrator** that leverages AI to dynamically generate, execute, and analyze tests across multiple distributed services communicating via **Apache Kafka** and **REST**.

The orchestrator enhances traditional test automation by integrating **Large Language Models (LLMs)** such as OpenAI's GPT to:
- Generate test cases and synthetic data,
- Monitor and validate Kafka events,
- Analyze logs for anomalies and failure patterns,
- Adapt to changing APIs and contracts without manual intervention.

The goal of this project is to showcase the intersection of **modern software testing**, **event-driven architecture**, and **artificial intelligence** — all built using **Java**, **Spring Boot**, **Kafka**, and **Docker**.

---

## 🧱 System Architecture

The system is composed of the following components:

- **UserService** – handles user registration and updates.
- **OrderService** – processes orders and emits events.
- **InventoryService** – manages stock levels and responds to order events.
- **Apache Kafka** – provides asynchronous event streaming between services.
- **AI Test Orchestrator** – listens to events and APIs, generates and runs tests, validates results, and logs test coverage.

Communication flow:
1. Services expose REST endpoints.
2. Events are emitted to Kafka topics (e.g., `user.events`, `order.events`).
3. The Test Orchestrator listens to events and APIs, generates test input, triggers workflows, and performs validations.
4. Optionally: Log data can be processed via ElasticSearch/Splunk for additional analysis.

**📌 Diagrams will be added later (Excalidraw).**

---

## 🧪 Testing Strategy

The Test Orchestrator supports a hybrid testing strategy combining:

### 🔹 Contract Testing
- Verifies that service interfaces conform to shared expectations using Pact.

### 🔹 Integration Testing
- Ensures Kafka message formats and inter-service communication are valid.

### 🔹 End-to-End (E2E) Testing
- Tests full flows, such as "User registration → Order creation → Inventory update".

### 🔹 AI-Enhanced Testing
- LLM is used to:
    - Generate JSON test payloads (valid and invalid cases),
    - Suggest new test scenarios based on existing OpenAPI specs,
    - Adjust test cases to API evolution.

### 🔹 Log-Based Anomaly Detection
- Uses AI to analyze logs and detect unusual patterns or regressions after deployments.

---

## 🤖 AI Integration

| Use Case                          | Description                                                                  |
|-----------------------------------|------------------------------------------------------------------------------|
| 🧬 Test Case Generation           | Dynamically creates input/output test data using GPT, based on OpenAPI specs |
| 🔍 Log Analysis                   | Parses service logs for failure patterns or anomalies                        |
| 🔁 Self-healing Tests             | Suggests adjustments when assertions fail due to API evolution               |
| 📊 Risk-based Test Prioritization | Weighs tests based on likelihood of failure using past test history          |

---

## ⚙️ Technology Stack

| Component            | Technology                          |
|----------------------|-------------------------------------|
| Language             | Java 17                             |
| Frameworks           | Spring Boot 3, Spring Kafka         |
| Communication        | REST (Spring Web), Apache Kafka     |
| Data Storage         | PostgreSQL (H2 for unit testing)    |
| Testing Frameworks   | TestNG, REST Assured                |
| AI Integration       | OpenAI GPT API (or local LLM)       |
| Logging & Monitoring | Splunk (log aggregation & analysis) |
| Build & CI/CD        | Maven, GitHub Actions               |
| Containerization     | Docker, Docker Compose              |

---

### Frameworks Explanation

This project uses two complementary Spring modules:

- **Spring Boot 3** is the foundational framework that provides autoconfiguration, dependency injection, and integration with core application components such as REST APIs, configuration, and testing utilities.
- **Spring Kafka** is a dedicated Spring module for integrating with Apache Kafka. It simplifies the creation of Kafka producers and consumers using annotations like `@KafkaListener`, enables easy deserialization, and supports embedded Kafka for testing purposes.

These two modules work together — Spring Boot provides the base infrastructure, while Spring Kafka focuses on Kafka-specific messaging features.

---

### Data Storage Explanation

**PostgreSQL** is used as the primary relational database across all services due to its robustness, SQL compliance, and wide ecosystem support.

For fast and isolated testing, especially during unit and integration test phases, **H2** is used as an in-memory database. This allows for lightweight test execution without needing to spin up PostgreSQL containers in every test run.

---

### Testing Frameworks Explanation

**TestNG** is used as the primary test framework in this project due to its robust support for data-driven testing (e.g., via `@DataProvider`), fine-grained test configuration options, and strong compatibility with REST Assured — the chosen library for testing RESTful APIs.

This combination provides a flexible and expressive setup for writing integration, contract, and E2E tests in an event-driven microservices environment.

---

### AI Integration Explanation

The AI integration in this project centers around the use of **Large Language Models (LLMs)** to enhance and automate the testing lifecycle. OpenAI's GPT API is used to dynamically generate realistic test data, construct edge cases, and assist in designing test scenarios based on OpenAPI specifications.

Additionally, AI is leveraged to analyze logs for anomalies, assist in failure diagnostics, and suggest self-healing strategies when API behaviors evolve. This makes the test orchestration process more adaptive, data-rich, and capable of detecting non-obvious issues during service interactions.

---

### Logging & Monitoring Explanation

**Splunk** is used to centralize logs from all microservices and the Test Orchestrator. It enables real-time collection, search, and analysis of structured and unstructured logs. This makes it easier to trace workflows, detect failures, and investigate anomalies in the system.

The logs are also analyzed by the AI engine to identify patterns of failure and suggest improvements to test scenarios. This approach ensures that the orchestration logic remains resilient and adaptive over time.

Grafana is not included in this setup, as the project focuses on log-based observability rather than metrics visualization.

---

### Build & CI/CD Explanation

The project uses **Maven** as the primary build tool for compiling, testing, and packaging the Java applications. Dependency management, test lifecycle handling, and plugin configuration are centralized in Maven for consistency across services.

**GitHub Actions** is used as the CI/CD platform to automate tasks such as building, running tests, and deploying Docker images. This allows for continuous validation and integration of code changes across all services and orchestrator components.

---

### Containerization Explanation

Each component of the system, including all microservices and the AI Test Orchestrator, is containerized using **Docker**. This ensures a consistent runtime environment and simplifies local development, testing, and deployment.

**Docker Compose** is used to orchestrate all containers locally, allowing for straightforward setup of Kafka, services, and supporting infrastructure with a single command. This supports quick onboarding, reproducible test runs, and scalable architecture design.

---

---

### Build & CI/CD Explanation

The project uses **Maven** as the primary build tool for compiling, testing, and packaging the Java applications. Dependency management, test lifecycle handling, and plugin configuration are centralized in Maven for consistency across services.

**GitHub Actions** is used as the CI/CD platform to automate tasks such as building, running tests, and deploying Docker images. This allows for continuous validation and integration of code changes across all services and orchestrator components.

---

### Containerization Explanation

Each component of the system, including all microservices and the AI Test Orchestrator, is containerized using **Docker**. This ensures a consistent runtime environment and simplifies local development, testing, and deployment.

**Docker Compose** is used to orchestrate all containers locally, allowing for straightforward setup of Kafka, services, and supporting infrastructure with a single command. This supports quick onboarding, reproducible test runs, and scalable architecture design.

---

---

##  Getting Started (To be expanded)

Basic instructions for local setup will be added here later.

Expected commands:
- `docker-compose up`
- `mvn clean install`
- `curl localhost:8081/api/...` to test endpoints
- Example Postman collections and Kafka topic consumers

---

##  Roadmap (Preview)

A detailed project roadmap and epics will be provided in the next step.

Key milestones:
- [ ] MVP: Functional microservices + Orchestrator with simple test
- [ ] LLM integration for test generation
- [ ] Advanced analytics and log ingestion
- [ ] UI for monitoring test coverage and execution status (optional)

---

## 📁 Repository Structure (Preview)