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

### Run maven clean install
JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH" ./mvnw clean install

### Test run App
JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH" ./mvnw -Dtest=DemoApplicationTests test

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

JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH" mvn spring-boot:run


JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home \
PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH" \
mvn spring-boot:run


# Run Locally
JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home \
PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH" \
mvn spring-boot:run


# Comply Endpoint

curl -i -X POST http://localhost:8080/api/comply/process \
  -H "Content-Type: application/json" \
  -d '{"id":null,"name":"Acme Corp","email":"compliance@acme.com"}'

# Comply GET endpoint
curl http://localhost:8080/companys

**Last Updated:** January 9, 2026

Yes. In the copy you’re running, start with:
[REFACTORING_COMPLETE.md (line 1)](/Users/karenheredia/projects-air/comply-api-blueprint/REFACTORING_COMPLETE.md:1) — architecture diagram, layers, request flows, endpoints, and future phases.
[REFACTORING_SUMMARY.md (line 1)](/Users/karenheredia/projects-air/comply-api-blueprint/REFACTORING_SUMMARY.md:1) — shorter architecture overview.
[ANGULAR_INTEGRATION_GUIDE.md (line 1)](/Users/karenheredia/projects-air/comply-api-blueprint/ANGULAR_INTEGRATION_GUIDE.md:1) — request/response examples.
[MANUAL_TEST_WITH_CURL.md (line 1)](/Users/karenheredia/projects-air/comply-api-blueprint/MANUAL_TEST_WITH_CURL.md:1) — endpoint testing instructions.


For real machine-readable compliance content, the best starting point is NIST OSCAL. It provides versioned XML, JSON, and YAML models, schemas, catalogs, profiles, and assessment artifacts:
NIST OSCAL
OSCAL downloads and content sources
Official NIST OSCAL content repository
For specific frameworks, use the official sources:
PCI DSS document library
CIS Benchmarks
Those files cannot be sent directly to the current /api/comply/process endpoint. The next architecture step would be an import endpoint such as POST /api/comply/artifacts that accepts OSCAL catalogs or assessment results, validates them, and maps controls/evidence into COMPLY’s own data model.


{
  "dataset_name": "CustomerTransactions",
  "jurisdictions": ["EU"],
  "business_role": "controller",
  "processing_purposes": ["fraud_detection"],
  "processing_activities": [
    "collection",
    "storage",
    "analytics",
    "inference"
  ],
  "retention_days": 365,
  "fields": [
    {
      "name": "email",
      "type": "string",
      "sample_hint": "person@example.com"
    },
    {
      "name": "amount",
      "type": "decimal"
    },
    {
      "name": "ip_address",
      "type": "string",
      "sample_hint": "192.0.2.10"
    }
  ]
}

The service returns a response similar to:

{
  "scan_id": "scan_123",
  "dataset_name": "CustomerTransactions",
  "overall_risk": "high",
  "detected_data_categories": [
    {
      "category": "contact_information",
      "fields": ["email"],
      "confidence": 0.99,
      "detection_method": "deterministic_rule"
    },
    {
      "category": "online_identifier",
      "fields": ["ip_address"],
      "confidence": 0.98,
      "detection_method": "deterministic_rule"
    }
  ],
  "applicable_frameworks": [
    {
      "framework": "GDPR",
      "applicability": "likely",
      "risk": "high",
      "references": [
        {
          "reference": "Article 5",
          "reason": "Personal data processing requires purpose limitation, minimization, and storage limitation."
        },
        {
          "reference": "Article 6",
          "reason": "A lawful basis is required for the declared processing purpose."
        },
        {
          "reference": "Article 32",
          "reason": "Personal data requires appropriate technical and organizational security measures."
        }
      ]
    }
  ],
  "recommended_controls": [
    {
      "control": "pseudonymize_email",
      "priority": "high",
      "reason": "Reduce direct identifier exposure in analytics and inference workflows."
    },
    {
      "control": "mask_ip_address",
      "priority": "medium",
      "reason": "Reduce unnecessary precision when full address retention is not required."
    },
    {
      "control": "document_lawful_basis",
      "priority": "high",
      "reason": "The declared processing purpose requires a documented legal basis."
    }
  ],
  "assumptions": [],
  "warnings": [
    "This result is an analytical compliance mapping and not legal advice."
  ],
  "rule_set_version": "2026.1",
  "generated_at": "2026-07-25T00:00:00Z"
}

The exact contract may be refined during specification, but changes must be justified.

# Analyze Request Response
karenheredia@MacBookAir comply-api-blueprint % curl -sS -X POST http://localhost:8080/v1/datasets/analyze \
  -H 'Content-Type: application/json' \
  -H 'X-Correlation-ID: local-demo' \
  -d '{
    "dataset_name": "CustomerTransactions",
    "jurisdictions": ["EU"],
    "business_role": "controller",
    "processing_purposes": ["fraud_detection"],
    "processing_activities": ["collection", "storage", "analytics"],
    "retention_days": 365,
    "fields": [
      {"name": "email", "type": "string"},
      {"name": "phone", "type": "string"},
      {"name": "ip_address", "type": "string"}
    ]
  }'
{"scan_id":"scan_0b72f2b5d48f946b08418e2f","dataset_name":"CustomerTransactions","overall_risk":"medium","risk_factors":[{"factor":"direct_identifiers","points":2,"reason":"The dataset contains fields that can identify or single out people or devices."},{"factor":"high_risk_processing_purpose","points":2,"reason":"The declared purpose may involve elevated monitoring, fraud, marketing, or model risk."}],"detected_data_categories":[{"category":"contact_information","fields":["email"],"confidence":0.99,"detection_method":"deterministic_rule","evidence":[{"field":"email","signal":"Field name matches email alias","source":"field_name"}]},{"category":"online_identifier","fields":["ip_address"],"confidence":0.99,"detection_method":"deterministic_rule","evidence":[{"field":"ip_address","signal":"Field name matches IP-address alias","source":"field_name"}]},{"category":"telephone_number","fields":["phone"],"confidence":0.99,"detection_method":"deterministic_rule","evidence":[{"field":"phone","signal":"Field name matches telephone alias","source":"field_name"}]}],"applicable_frameworks":[{"framework":"CCPA/CPRA","framework_type":"law","framework_version":"California Consumer Privacy Act as amended","applicability":"likely","risk":"medium","rule_ids":["CCPA_CPRA.PERSONAL_INFORMATION.001"],"references":[{"reference":"Cal. Civ. Code §1798.100","title":"Right to know and notice of collection","reason":"Personal-information fields warrant review of collection notice and purpose disclosures.","triggering_facts":["data_categories=contact_information,online_identifier,telephone_number"],"rule_id":"CCPA_CPRA.PERSONAL_INFORMATION.001"},{"reference":"Cal. Civ. Code §1798.105","title":"Right to delete","reason":"The data model should support retention and deletion workflow review.","triggering_facts":["data_categories=contact_information,online_identifier,telephone_number"],"rule_id":"CCPA_CPRA.PERSONAL_INFORMATION.001"}],"triggering_facts":["data_categories=contact_information,online_identifier,telephone_number"],"missing_information":[]},{"framework":"GDPR","framework_type":"law","framework_version":"Regulation (EU) 2016/679","applicability":"likely","risk":"medium","rule_ids":["GDPR.PERSONAL_DATA.001"],"references":[{"reference":"Article 5","title":"Principles relating to processing of personal data","reason":"Personal data processing should address purpose limitation, data minimisation, and storage limitation.","triggering_facts":["data_categories=contact_information,online_identifier,telephone_number"],"rule_id":"GDPR.PERSONAL_DATA.001"},{"reference":"Article 6","title":"Lawfulness of processing","reason":"A lawful basis is required for the declared processing purpose.","triggering_facts":["data_categories=contact_information,online_identifier,telephone_number"],"rule_id":"GDPR.PERSONAL_DATA.001"}],"triggering_facts":["data_categories=contact_information,online_identifier,telephone_number"],"missing_information":[]}],"recommended_controls":[{"control":"apply_data_minimization","priority":"high","type":"preventive","reason":"Retain only fields necessary for the declared purpose.","triggering_facts":["data_categories=contact_information,online_identifier,telephone_number"],"rule_id":"GDPR.PERSONAL_DATA.001"},{"control":"document_lawful_basis","priority":"high","type":"governance","reason":"Record the legal basis and purpose for the declared processing.","triggering_facts":["data_categories=contact_information,online_identifier,telephone_number"],"rule_id":"GDPR.PERSONAL_DATA.001"},{"control":"provide_collection_notice","priority":"high","type":"governance","reason":"Document categories collected and the purposes for collection.","triggering_facts":["data_categories=contact_information,online_identifier,telephone_number"],"rule_id":"CCPA_CPRA.PERSONAL_INFORMATION.001"},{"control":"define_retention_schedule","priority":"medium","type":"governance","reason":"Avoid retaining personal information longer than reasonably necessary.","triggering_facts":["data_categories=contact_information,online_identifier,telephone_number"],"rule_id":"CCPA_CPRA.PERSONAL_INFORMATION.001"},{"control":"honor_consumer_rights","priority":"medium","type":"corrective","reason":"Establish a process for applicable access and deletion requests.","triggering_facts":["data_categories=contact_information,online_identifier,telephone_number"],"rule_id":"CCPA_CPRA.PERSONAL_INFORMATION.001"}],"assumptions":["Unspecified optional processing flags were treated as false for deterministic evaluation."],"warnings":["This result is compliance-analysis support and not legal advice.","Framework mappings are indicators based on supplied metadata, not final legal determinations."],"api_version":"v1","engine_version":"0.1.0","rule_set_version":"2026.1","correlation_id":"local-demo","generated_at":"2026-07-25T23:52:51.098071Z"}% 


# Verify if Java process is occupied 
ps -fp 67735

# Stop the current port and rerun
kill 67735
./mvnw spring-boot:run

# run the app on another port without stopping it
./mvnw spring-boot:run \
  -Dspring-boot.run.arguments=--server.port=8081