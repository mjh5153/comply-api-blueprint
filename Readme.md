# COMPLY API Blueprint

[![CI](https://github.com/mjh5153/comply-api-blueprint/actions/workflows/ci.yml/badge.svg)](https://github.com/mjh5153/comply-api-blueprint/actions/workflows/ci.yml)

A production-grade **Spring Boot 3.5** REST API demonstrating idiomatic
concurrent and asynchronous request handling with `CompletableFuture`,
Spring Data JPA persistence, and clean layered architecture.

Built as a portfolio project — the codebase is intentionally small and
focused so reviewers can read every class in a few minutes.

---

## Highlights

- **Sync + async CRUD** for a `Company` resource, both variants sharing the
  same service and persistence layer.
- **Batch async endpoint** that fans out per-item `CompletableFuture`s and
  aggregates them with `CompletableFuture.allOf` for parallel throughput.
- **Concurrent external-HTTP fan-out** via `java.net.http.HttpClient`
  (`AsyncHttpService`), wrapped as a Spring service.
- **Custom `@Async` executor** (10 core / 20 max threads, 100-item queue)
  configured in `AsyncConfig`.
- **Layered structure** — controller → service (interface + impl) → mapper
  → repository → JPA entity → DTO record.
- **H2 by default**, MySQL opt-in via a Spring profile, credentials read
  from environment variables (no secrets in the repo).
- **MockMvc integration tests** that exercise every controller.
- **Unit tests with Mockito** for the service layer (fast, no Spring context).
- **OpenAPI 3 / Swagger UI** auto-generated at `/swagger-ui.html`.
- **Dockerised** — multi-stage build produces a slim JRE image.
- **GitHub Actions CI** builds and tests on every push / PR.

## Tech stack

| Layer      | Technology                                              |
|------------|---------------------------------------------------------|
| Runtime    | Java 17+, Spring Boot 3.5.6                              |
| Web        | Spring Web MVC, Jackson                                  |
| Persistence| Spring Data JPA, Hibernate 6.6, H2 (default) / MySQL     |
| Async      | `CompletableFuture`, Spring `@Async`, `ThreadPoolTaskExecutor` |
| Build      | Maven 3.9+                                               |
| Tests      | JUnit 5, Spring Boot Test, MockMvc                       |

## Project layout

```
src/main/java/com/init_spring_bean_mvn/demo/
├── DemoApplication.java          # Spring Boot entry point
├── controller/
│   ├── CompanyController.java    # /companys (sync + async CRUD)
│   └── ComplyController.java     # /api/comply (compliance workflows)
├── service/
│   ├── CompanyService.java
│   ├── ComplyApiService.java
│   ├── AsyncHttpService.java
│   └── impl/                     # implementations
├── repository/CompanyRepository.java
├── mapper/CompanyMapper.java
├── entity/Company.java           # JPA entity
├── dto/CompanyDTO.java           # immutable record DTO
├── exception/ResourceNotFoundException.java
├── config/AsyncConfig.java       # thread pool
└── util/ConcurrentFileWriter.java

src/main/resources/
├── application.properties        # H2 defaults
└── application-mysql.properties  # opt-in MySQL profile

src/test/java/…                   # MockMvc integration tests
```

## Quick start

Requires **JDK 17+** and **Maven 3.6+** (this repo has no `mvnw` wrapper).

```bash
# Build + test
mvn clean verify

# Run
mvn spring-boot:run
```

The app starts on **http://localhost:8080** with an in-memory H2 database.

### Smoke-test the API

```bash
# Create a company
curl -s -X POST http://localhost:8080/companys \
  -H 'Content-Type: application/json' \
  -d '{"id":null,"name":"Acme","email":"acme@example.com"}'

# List all
curl -s http://localhost:8080/companys

# Batch async create
curl -s -X POST http://localhost:8080/companys/batch/async \
  -H 'Content-Type: application/json' \
  -d '[{"id":null,"name":"B1","email":"b1@x.com"},{"id":null,"name":"B2","email":"b2@x.com"}]'
```

### API docs (Swagger UI)

Once the app is running:

- Interactive UI → **http://localhost:8080/swagger-ui.html**
- Raw OpenAPI 3 JSON → **http://localhost:8080/v3/api-docs**

### Run with Docker

```bash
docker build -t comply-api-blueprint .
docker run --rm -p 8080:8080 comply-api-blueprint
```

The multi-stage Dockerfile builds with Maven + JDK 17, then ships a slim
`eclipse-temurin:17-jre-alpine` runtime image.

### Deploy to Render (free tier)

A `render.yaml` blueprint is included, so hosting takes ~2 minutes:

1. Push this repo to GitHub.
2. In the [Render dashboard](https://dashboard.render.com/) click **New +** →
   **Blueprint** and select this repo.
3. Render reads `render.yaml`, builds the `Dockerfile`, and gives you a URL
   like `https://comply-api-blueprint.onrender.com`.

Once deployed, browse:
- `https://<your-app>.onrender.com/swagger-ui.html`
- `https://<your-app>.onrender.com/companys`

> Render's free tier sleeps the service after 15 minutes idle, so the first
> request after a nap takes 30–60 s to cold-start. Fine for a portfolio,
> upgrade to a paid plan for zero-downtime.

## API surface

### `CompanyController` — `/companys`

| Method | Path              | Description                                     |
|--------|-------------------|-------------------------------------------------|
| GET    | `/companys`       | List all companies                              |
| GET    | `/companys/{id}`  | Get by id (`404` if missing)                    |
| POST   | `/companys`       | Create (sync)                                   |
| PUT    | `/companys/{id}`  | Update (sync)                                   |
| DELETE | `/companys/{id}`  | Delete                                          |
| POST   | `/companys/async` | Create (non-blocking `CompletableFuture`)       |
| POST   | `/companys/batch/async` | Parallel batch create                     |
| PUT    | `/companys/{id}/async`  | Update (non-blocking)                     |

### `ComplyController` — `/api/comply`

| Method | Path                              | Description                            |
|--------|-----------------------------------|----------------------------------------|
| POST   | `/api/comply/process`             | Async single compliance request        |
| POST   | `/api/comply/process/batch`       | Parallel batch compliance              |
| POST   | `/api/comply/external-api/concurrent` | Fan out concurrent HTTP calls      |
| POST   | `/api/comply/reconcile`           | Aggregate/reconcile API responses      |

## Configuration

### Default profile (H2, in-memory)

Nothing to configure — everything is preset in `application.properties`.

### MySQL profile

Set environment variables and activate the profile:

```bash
export DB_URL=jdbc:mysql://localhost:3306/ems
export DB_USERNAME=your_user
export DB_PASSWORD=your_password
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

## Design notes

- **Records for DTOs** (`CompanyDTO`) — immutable, no boilerplate,
  serialised natively by Jackson.
- **Constructor injection everywhere** — no field injection, no
  `@Autowired` on fields, all collaborators are `final`.
- **Async patterns** — service methods return `CompletableFuture<T>`
  scheduled on a custom `ThreadPoolTaskExecutor`; controllers return
  `CompletableFuture<ResponseEntity<T>>` so the servlet container releases
  the request thread while work runs.
- **Errors** — `ResourceNotFoundException` is annotated
  `@ResponseStatus(NOT_FOUND)`, mapping cleanly to `404`. Async chains use
  `.exceptionally(...)` to convert failures into `500`.
- **Thread-safe file writes** — `ConcurrentFileWriter` uses `ReentrantLock`
  in a `try/finally` for safe concurrent append operations.

## License

MIT
