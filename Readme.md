# Demo - Full Stack Java Spring Boot Application

A comprehensive Spring Boot application demonstrating concurrent request handling, async operations, HTTP server management, and REST API development with database integration.

## Project Overview

This project showcases:
- **Concurrent Request Processing** - Asynchronous HTTP client operations with thread-safe file handling
- **HTTP Server Implementation** - Custom OrderFulfillmentServer with JSON response handling
- **Spring Boot REST APIs** - RESTful controllers with full CRUD operations
- **Database Integration** - JPA/Hibernate with Spring Data
- **Compliance API** - Foundation for COMPLY API blueprint integration

## Tech Stack

### Backend
- **Java 17+** - Modern Java with latest features
- **Spring Boot 3.5.6** - Enterprise application framework
- **Spring Data JPA** - Object-relational mapping
- **Hibernate 6.6** - ORM framework
- **H2/MySQL** - Database options

### Build & Dependencies
- **Maven** - Build automation
- **Jackson** - JSON processing
- **Lombok** - Boilerplate reduction

## Project Structure

```
src/
├── main/
│   ├── java/com/init_spring_bean_mvn/demo/
│   │   ├── controller/         # REST API endpoints
│   │   ├── service/            # Business logic
│   │   ├── entity/             # JPA entities
│   │   ├── dto/                # Data transfer objects
│   │   ├── persistence/        # Repository layer
│   │   ├── networking/         # HTTP client operations
│   │   ├── httpserver/         # Custom HTTP server
│   │   └── ...
│   └── resources/
│       └── application.properties
└── test/
    └── java/...
```

## Key Components

### 1. PostConcurrentRequeststoServer
Demonstrates asynchronous HTTP operations with concurrent request handling:
- Creates multiple POST requests concurrently
- Uses `CompletableFuture` for async processing
- Implements thread-safe file writing with `ReentrantLock`
- Handles response callbacks asynchronously

**Features:**
- Stream-based request building
- Async callback patterns for response handling
- Thread-safe file I/O operations

### 2. OrderFulfillmentServer
Custom HTTP server implementation using `com.sun.net.httpserver`:
- Lightweight HTTP server on port 8080
- Handles GET and POST requests
- Generates JSON order responses
- Unique order ID generation with `AtomicLong`
- Request parameter parsing

**Response Format:**
```json
{
  "order": {
    "orderId": "0000000001",
    "product": "cantaloupes",
    "amount": 100,
    "orderReceivedData": "2026-01-09T11:36:28.886669",
    "orderDeliveryData": "2026-01-12"
  }
}
```

### 3. CompanyController
REST API controller for company management:
- GET endpoints for retrieving companies
- POST endpoints for creating companies
- PUT endpoints for updating companies
- Path variables and request parameters
- DTO/Entity mapping

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+ (or use H2 for development)

### Installation

1. **Clone the repository:**
   ```bash
   git clone git@github.com:mjh5153/demo.git
   cd demo
   ```

2. **Build the project:**
   ```bash
   ./mvnw clean install
   ```

3. **Configure database:**
   Update `src/main/resources/application.properties` with your database details

4. **Run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```

The application will start on `http://localhost:8080`

## API Endpoints

### Company Management
- `GET /companys` - Get all companies
- `GET /companys/company` - Get single company
- `GET /companys/{id}/{name}` - Get company by path variables
- `GET /companys/query?id=1&name=Karen` - Get company by query parameters
- `POST /companys` - Create new company (DTO)
- `POST /companys/create` - Create new company (Entity)
- `PUT /companys/{id}` - Update company (DTO)
- `PUT /companys/{id}/update` - Update company (Entity)

## Concurrent Request Example

The `PostConcurrentRequeststoServer` demonstrates sending multiple concurrent requests:

```java
Map<String, Integer> orderMap = Map.of(
    "apples", 500, 
    "oranges", 1000, 
    "bananas", 75,
    "carrots", 2000,
    "cantaloupes", 100
);

// Sends 5 async POST requests to the order fulfillment server
// Responses are written to orderTracking.json with thread-safe locking
```

## Performance Optimizations

- **Async Processing** - CompletableFuture for non-blocking operations
- **Thread Safety** - ReentrantLock for concurrent file access
- **Connection Pooling** - HikariCP for database connections
- **Stream API** - Functional programming for data processing

## Code Guidelines

This project follows strict coding standards:

### Naming Conventions
- `PascalCase` - Classes, interfaces, components
- `camelCase` - Variables, methods, functions
- `_prefix` - Private class members
- `ALL_CAPS` - Constants

### Error Handling
- Try/catch blocks for async operations
- Proper error logging with context
- Exception propagation where appropriate

### Documentation
- Clear, concise documentation
- Code examples for complex operations
- Present tense documentation
- Active voice in descriptions

See `.github/copilot-instructions.md` for full guidelines.

## Upcoming Features

- **COMPLY API Blueprint Integration** - Angular frontend integration
- **Database Persistence** - Scan entity storage
- **WebFlux Integration** - Reactive patterns for high-performance scenarios
- **Caching Layer** - Redis/Ehcache for performance optimization

## Database Configuration

### MySQL Setup
Add to `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/demo_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### H2 (Development)
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.h2.console.enabled=true
```

## Contributing

Follow the coding standards in `.github/copilot-instructions.md` and `.github/chat-instructions.md`

## License

This project is part of the COMPLY API initiative.

## Author

**GitHub:** [@mjh5153](https://github.com/mjh5153)

## Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Java HTTP Client API](https://docs.oracle.com/en/java/javase/17/docs/api/java.net.http/java/net/http/HttpClient.html)
- [Hibernate Documentation](https://hibernate.org/orm/documentation/)
- [COMPLY API Blueprint](git@github.com:mjh5153/comply-api-blueprint.git)

---

**Last Updated:** January 9, 2026

