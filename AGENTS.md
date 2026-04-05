# AGENTS.md

## Snapshot first
- Treat this repo as a **documented skeleton**, not a finished microservice system. The architecture is described in `README.md`, `system-logic.md`, `infrastructure.md`, and `Docs/*.puml`, but the code currently contains only three placeholder apps and minimal Maven module descriptors.
- Source of truth for the intended business flow: `system-logic.md` + `Docs/flow.uml`.
- Source of truth for runtime/container topology: `infrastructure.md` + `Docs/infrastructure_diagram.puml`.

## Repository shape
- Root `pom.xml` is an aggregator (`packaging=pom`) for `user-service`, `order-service`, and `inventory-service`.
- Each service has its own Maven module, `Dockerfile`, `README.md`, and package namespace under `com.portfolio.*service`.
- Current Java entrypoints are only placeholders: `user-service/.../UserServiceApplication.java`, `order-service/.../OrderServiceApplication.java`, `inventory-service/.../InventoryServiceApplication.java`.
- There are currently **no discovered tests**, controllers, entities, repositories, Kafka configs, or Compose files in the workspace.

## Intended service boundaries
- `user-service`: validates or manages users; other services should treat it as the user authority.
- `order-service`: owns **order state** and should persist statuses such as `PENDING_INVENTORY`, `RESERVED`, `COMPLETED`, `REJECTED` (see `system-logic.md`).
- `inventory-service`: owns **fulfillment decisions** and should publish lifecycle outcomes; `order-service` reacts to those events instead of polling.
- Communication pattern is intentionally hybrid: **REST for commands**, **Kafka for state propagation**.
- Event ordering is expected to be keyed by `orderId` (`infrastructure.md`). Preserve that assumption if you add producers/consumers.

## Important current constraints
- The project is now aligned on Java 23 across module `pom.xml` files, `.idea/misc.xml`, `README.md`, and all service `Dockerfile`s. Keep new build/runtime config on Java 23 unless you are intentionally planning a downgrade.
- All `application.yml` files currently bind `server.port: 8080`; this works only if services run separately. If you introduce local multi-service startup, ports must diverge or be container-mapped.
- `README.md` mentions Kafka, PostgreSQL, H2, TestNG, REST Assured, Docker Compose, Splunk, and an AI orchestrator, but these are **architectural intentions**, not implemented dependencies in the current codebase.

## Verified developer workflow
- Root-level Maven validation succeeds:
  - `mvn -q validate`
- Root-level Maven test phase also succeeds in the current skeleton:
  - `mvn -q test`
- Do not assume `spring-boot:run`, packaged JARs, or Docker Compose work yet; no Spring Boot plugin/dependencies or compose file were found.

## Editing conventions for this repo
- Keep changes **module-local** unless you are intentionally changing cross-service contracts.
- When implementing the documented architecture, update both code and the matching design docs (`README.md`, `system-logic.md`, `infrastructure.md`) if behavior diverges.
- Follow the package split already established by module: `com.portfolio.userservice`, `com.portfolio.orderservice`, `com.portfolio.inventoryservice`.
- Prefer making the “current state vs planned architecture” explicit in docs and PRs; this repo already mixes both.
- If you add shared event schemas or client contracts, place them where all three modules can consume them consistently and document the ownership boundary.

## High-value files to read before coding
- `README.md`
- `system-logic.md`
- `infrastructure.md`
- `project-roadmap.md`
- `Docs/flow.uml`
- `Docs/infrastructure_diagram.puml`
- Root and module `pom.xml` files
- Service `application.yml` and `Dockerfile` files

