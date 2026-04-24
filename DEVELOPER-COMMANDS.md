# Developer Commands

Practical cheat sheet for the current runtime skeleton. Extend this file as the project evolves.

## Maven
Basic project validation from the repository root:

```zsh
mvn -q validate
mvn -q package -DskipTests
```

What they do:
- `validate` — checks Maven structure and configuration.
- `package -DskipTests` — builds executable JARs for all three services.

### Test profiles

The project uses two test profiles. Both run the same test suite against different environments:

```zsh
mvn test -P h2            # H2 in-memory + embedded Kafka — no Docker required
mvn test -P integration   # real PostgreSQL + Kafka via Testcontainers — Docker required
```

- `-P h2` — fast, Docker-free. H2 runs in JVM, embedded Kafka starts automatically.
- `-P integration` — production-faithful. Testcontainers manages PostgreSQL and Kafka containers automatically; no manual `docker compose up` needed.

## Docker Compose
Commands to run from the repository root:

```zsh
# Validate Compose file syntax
docker compose config

# Build service images
docker compose build

# Start all services (foreground, streaming logs)
docker compose up

# Start all services and rebuild images beforehand
docker compose up --build

# Start all services in the background (detached)
docker compose up -d

# Show status of running services
docker compose ps

# Stream logs from all services
docker compose logs -f

# Stream logs from a single service
docker compose logs -f user-service

# Stop all running services (keeps containers)
docker compose stop

# Stop and remove containers, networks (keeps volumes)
docker compose down

# Stop and remove containers, networks, AND volumes (full reset)
docker compose down -v
```

Notes:
- `docker compose config` checks syntax and the fully resolved Compose configuration.
- `docker compose build` requires a running Docker daemon.
- The current `docker-compose.yml` maps host ports as follows:
  - `user-service` → `8081`
  - `order-service` → `8082`
  - `inventory-service` → `8083`
  - `kafka` → `29092`
  - `user-db`, `order-db`, `inventory-db` → PostgreSQL (no host port exposed)

## Start a single container

```zsh
# Start one service (and its declared dependencies)
docker compose up user-service
docker compose up order-service
docker compose up inventory-service

# Start one service in the background
docker compose up -d user-service

# Start one service and rebuild its image first
docker compose up --build user-service

# Restart a single running service
docker compose restart user-service
```

Notes:
- Starting a service also brings up any services listed under its `depends_on`.
- Use the service name exactly as it appears in `docker-compose.yml`.

## Docker daemon / status

```zsh
# Check daemon info and resource usage
docker info

# List all containers (running and stopped)
docker ps -a

# List local images
docker images
```

## Run JAR locally
After `mvn -q package -DskipTests`, you can run the services without Docker.

### user-service
```zsh
java -jar user-service/target/user-service-0.0.1-SNAPSHOT.jar --server.port=18081
```

### order-service
```zsh
java -jar order-service/target/order-service-0.0.1-SNAPSHOT.jar --server.port=18082
```

### inventory-service
```zsh
java -jar inventory-service/target/inventory-service-0.0.1-SNAPSHOT.jar --server.port=18083
```

## Health checks
Manual checks for the `/health` endpoints.

### When services run locally on custom ports
```zsh
curl http://127.0.0.1:18081/health
curl http://127.0.0.1:18082/health
curl http://127.0.0.1:18083/health
```

### When services run through Docker Compose
```zsh
curl http://127.0.0.1:8081/health
curl http://127.0.0.1:8082/health
curl http://127.0.0.1:8083/health
```

Expected responses look similar to:

```json
{"service":"order-service","status":"UP"}
```

## Smoke test helper: `run_check`
`run_check` **is not a system command and is not part of the repository**. It is a temporary shell function used for quick smoke tests.

Example invocation:

```zsh
run_check "inventory-service/target/inventory-service-0.0.1-SNAPSHOT.jar" 18083 "inventory-service"
```

This invocation means:
1. start the selected JAR in the background,
2. set the application port to `18083`,
3. call `http://127.0.0.1:18083/health`,
4. verify that the response contains `inventory-service`,
5. stop the process after the test.

If you want to do the same manually without the helper:

```zsh
java -jar inventory-service/target/inventory-service-0.0.1-SNAPSHOT.jar --server.port=18083
```

in a second terminal:

```zsh
curl http://127.0.0.1:18083/health
```

