# AGENTS.md

## Snapshot first
- Treat this repo as a **runtime skeleton guided by docs**, not a finished microservice system. The architecture is described in `README.md`, `Docs/system-logic.md`, `Docs/infrastructure.md`, and `Docs/*.puml`; the code now contains minimal Spring Boot service skeletons plus a local Compose setup, but not the full documented business flow.
- Source of truth for the intended business flow: `Docs/system-logic.md` + `Docs/flow.uml`.
- Source of truth for runtime/container topology: `Docs/infrastructure.md` + `Docs/infrastructure_diagram.puml`.
- AI agent guidance in this repo is currently centralized in this file (`AGENTS.md`); no additional agent-rule files (for example `.github/copilot-instructions.md`, `CLAUDE.md`, `.cursorrules`) are present.

## Repository shape
- Root `pom.xml` is an aggregator (`packaging=pom`) for `user-service`, `order-service`, and `inventory-service`.
- Each service has its own Maven module, `Dockerfile`, `README.md`, and package namespace under `com.portfolio.*service`.
- Current Java entrypoints are minimal Spring Boot apps with inline `/health` endpoints in `user-service/.../UserServiceApplication.java`, `order-service/.../OrderServiceApplication.java`, and `inventory-service/.../InventoryServiceApplication.java`.
- The workspace now includes `docker-compose.yml` plus multi-stage service `Dockerfile`s, but there are still **no discovered tests**, entities, repositories, service-to-service clients, or Kafka application configs in code.

## Intended service boundaries
- `user-service`: validates or manages users; other services should treat it as the user authority.
- `order-service`: owns **order state** and should persist statuses such as `PENDING_INVENTORY`, `RESERVED`, `COMPLETED`, `REJECTED` (see `Docs/system-logic.md`).
- `inventory-service`: owns **fulfillment decisions** and should publish lifecycle outcomes; `order-service` reacts to those events instead of polling.
- Communication pattern is intentionally hybrid: **REST for commands**, **Kafka for state propagation**.
- Event ordering is expected to be keyed by `orderId` (`Docs/infrastructure.md`). Preserve that assumption if you add producers/consumers.

## Important current constraints
- The project is now aligned on Java 23 across module `pom.xml` files, `.idea/misc.xml`, `README.md`, and all service `Dockerfile`s. Keep new build/runtime config on Java 23 unless you are intentionally planning a downgrade.
- All `application.yml` files currently bind `server.port: 8080`; local multi-service startup is handled via Compose port mappings `8081:8080`, `8082:8080`, `8083:8080`.
- `README.md` mentions Kafka, PostgreSQL, H2, TestNG, REST Assured, Docker Compose, Splunk, and an AI orchestrator, but these are **architectural intentions**, not implemented dependencies in the current codebase.
- `docker-compose.yml` currently wires only the three HTTP services plus Kafka (KRaft mode, `apache/kafka:3.9.0` — no Zookeeper); PostgreSQL, business endpoints, and Kafka producers/consumers are still not implemented.

## Verified developer workflow
- Root-level Maven validation succeeds:
  - `mvn -q validate`
- Root-level Maven test phase also succeeds in the current skeleton:
  - `mvn -q test`
- Root-level packaging now succeeds and produces runnable service JARs:
  - `mvn -q package -DskipTests`
- Compose syntax and image builds can be verified locally from repo root:
  - `docker compose config`
  - `docker compose build`
- For local non-Compose smoke checks (`java -jar ... --server.port=1808x` + `curl /health`), use the command catalog in `DEVELOPER-COMMANDS.md`.
- Do not assume the Compose stack implements the documented business flow yet; today it is a minimal health-checkable runtime skeleton.

## Editing conventions for this repo
- Keep changes **module-local** unless you are intentionally changing cross-service contracts.
- When implementing the documented architecture, update both code and the matching design docs (`README.md`, `Docs/system-logic.md`, `Docs/infrastructure.md`) if behavior diverges.
- Follow the package split already established by module: `com.portfolio.userservice`, `com.portfolio.orderservice`, `com.portfolio.inventoryservice`.
- Prefer making the “current state vs planned architecture” explicit in docs and PRs; this repo already mixes both.
- If you add shared event schemas or client contracts, place them where all three modules can consume them consistently and document the ownership boundary.

## CI / GitHub Actions
- `.github/workflows/health-check.yml` runs on every push and PR to `master`.
- The workflow builds all service JARs (`mvn -q package -DskipTests`), starts each service on a dedicated port (`18081`–`18083`), and verifies `/health` returns `"status":"UP"`.
- If you rename JAR artifacts or change the `/health` endpoint contract, update the workflow accordingly.

## High-value files to read before coding
- `README.md`
- `Docs/system-logic.md`
- `Docs/infrastructure.md`
- `Docs/project-roadmap.md`
- `DEVELOPER-COMMANDS.md`
- `Docs/flow.uml`
- `Docs/infrastructure_diagram.puml`
- Root and module `pom.xml` files
- Service `application.yml` and `Dockerfile` files
- `.github/workflows/health-check.yml`

