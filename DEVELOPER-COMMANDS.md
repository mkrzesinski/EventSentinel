# Developer Commands

Practical cheat sheet for the current runtime skeleton. Extend this file as the project evolves.

## Maven
Basic project validation from the repository root:

```zsh
mvn -q validate
mvn -q test
mvn -q package -DskipTests
```

What they do:
- `validate` — checks Maven structure and configuration.
- `test` — runs the test phase; in the current skeleton it passes without business tests.
- `package -DskipTests` — builds executable JARs for all three services.

## Docker Compose
Commands to run from the repository root:

```zsh
docker compose config
docker compose build
```

Notes:
- `docker compose config` checks syntax and the fully resolved Compose configuration.
- `docker compose build` requires a running Docker daemon.
- The current `docker-compose.yml` maps host ports as follows:
  - `user-service` → `8081`
  - `order-service` → `8082`
  - `inventory-service` → `8083`
  - `kafka` → `29092`

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

## Next extensions
Good candidates to add later:
- full `docker compose up --build`
- container healthchecks
- log commands (`docker compose logs`)
- commands for Kafka topics
- commands for future business endpoints

