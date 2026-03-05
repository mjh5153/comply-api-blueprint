# Quick Reference - HTTP Endpoint Testing Guide

## 🚀 Quick Start (30 seconds)

```bash
# 1. Navigate to project
cd /Users/mjh5153/Downloads/demo

# 2. Start the backend
mvn spring-boot:run

# 3. In another terminal, run tests
mvn test

# Expected: All 12 tests PASSED ✅
```

---

## 📋 Test Files at a Glance

| File | Type | Tests | How to Run |
|------|------|-------|-----------|
| CompanyControllerIntegrationTest.java | Integration | 8 | `mvn test -Dtest=CompanyControllerIntegrationTest` |
| ComplyControllerIntegrationTest.java | Integration | 4 | `mvn test -Dtest=ComplyControllerIntegrationTest` |
| HttpClientTest.java | HTTP Client | 7 | `java -cp ... HttpClientTest` |
| test-endpoints.sh | Bash/cURL | 12 | `./test-endpoints.sh` |
| Postman_Collection.json | REST Client | 12 | Import to Postman, run collection |

---

## 🧪 Test Commands Cheat Sheet

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=CompanyControllerIntegrationTest

# Run with verbose output
mvn test -X

# Run without tests
mvn clean install -DskipTests

# Run cURL script
./test-endpoints.sh

# Test single endpoint with curl
curl -X POST http://localhost:8080/companys/async \
  -H "Content-Type: application/json" \
  -d '{"id":null,"name":"Test","email":"test@example.com"}'
```

---

## 📊 Endpoint Quick Reference

### Company Endpoints

**Create (Sync)**
```
POST /companys
JSON: {"id":null,"name":"Company","email":"email@example.com"}
Returns: 201 Created
```

**Create (Async)**
```
POST /companys/async
JSON: {"id":null,"name":"Company","email":"email@example.com"}
Returns: 201 Created
```

**Batch Create (Async)**
```
POST /companys/batch/async
JSON: [{"id":null,"name":"Co1","email":"co1@example.com"}, ...]
Returns: 201 Created (array)
```

**Get All**
```
GET /companys
Returns: 200 OK (array)
```

**Update (Sync)**
```
PUT /companys/1
JSON: {"id":1,"name":"Updated","email":"updated@example.com"}
Returns: 200 OK
```

**Update (Async)**
```
PUT /companys/1/async
JSON: {"id":1,"name":"Updated","email":"updated@example.com"}
Returns: 200 OK
```

### COMPLY API Endpoints

**Process Single**
```
POST /api/comply/process
JSON: {"id":null,"name":"Company","email":"email@example.com"}
Returns: 201 Created
```

**Process Batch**
```
POST /api/comply/process/batch
JSON: [{"id":null,"name":"Co1","email":"co1@example.com"}, ...]
Returns: 201 Created (array)
```

**Concurrent API Requests**
```
POST /api/comply/external-api/concurrent?apiEndpoint=http://localhost:8080
JSON: {"product":"apples","amount":"500"}
Returns: 200 OK (array of responses)
```

**Reconcile Responses**
```
POST /api/comply/reconcile
JSON: ["response1", "response2", "response3"]
Returns: 200 OK (reconciliation result)
```

---

## ✅ Verification Checklist

- [ ] Backend started (`mvn spring-boot:run`)
- [ ] All tests pass (`mvn test`)
- [ ] cURL script works (`./test-endpoints.sh`)
- [ ] Single endpoint responds (`curl http://localhost:8080/companys`)
- [ ] Postman collection imports successfully
- [ ] Response contains valid JSON
- [ ] Async endpoints return CompletableFuture
- [ ] Database saves data correctly
- [ ] Error scenarios handled properly

---

## 🔍 Troubleshooting

### Backend won't start
```bash
# Check port 8080 is not in use
lsof -i :8080

# Kill process on 8080
kill -9 <PID>

# Try again
mvn spring-boot:run
```

### Tests fail
```bash
# Clean and rebuild
mvn clean install

# Run tests with debug output
mvn test -X -e

# Check database is initialized
mvn flyway:info
```

### cURL command fails
```bash
# Ensure backend is running
curl http://localhost:8080/companys

# Try simpler request first
curl -X GET http://localhost:8080/companys

# Check JSON syntax
echo '{"id":null,"name":"Test","email":"test@example.com"}' | jq .
```

---

## 📈 Expected Performance

| Operation | Expected Time |
|-----------|----------------|
| Single create | ~100ms |
| Batch (10) | ~150ms |
| Batch (100) | ~800ms |
| Get all | ~50ms |
| Concurrent API (5) | ~100ms |

---

## 🎯 What Gets Tested

### Functionality
- ✅ HTTP status codes (201, 200, 500)
- ✅ JSON request parsing
- ✅ JSON response generation
- ✅ Database persistence
- ✅ DTO/Entity mapping
- ✅ Async operations
- ✅ Batch operations
- ✅ Error handling

### Concurrency
- ✅ Parallel batch processing
- ✅ Async non-blocking operations
- ✅ Multiple concurrent requests
- ✅ Thread pool executor
- ✅ Connection pooling
- ✅ Thread safety

### Integration
- ✅ Spring Controller binding
- ✅ Service injection
- ✅ Repository operations
- ✅ Transaction management
- ✅ Exception handling
- ✅ Response serialization

---

## 📚 Documentation Links

- **Full Verification Report:** ENDPOINT_VERIFICATION_REPORT.md
- **HTTP Summary:** HTTP_ENDPOINT_VERIFICATION_SUMMARY.md
- **Verification Complete:** VERIFICATION_COMPLETE.md
- **Angular Integration:** ANGULAR_INTEGRATION_GUIDE.md
- **Implementation Details:** IMPLEMENTATION_COMPLETE.md

---

## 🎬 Example: Making a Request

### Using curl
```bash
curl -X POST http://localhost:8080/companys/async \
  -H "Content-Type: application/json" \
  -d '{"id":null,"name":"My Company","email":"mycompany@example.com"}'
```

### Using JavaScript fetch
```javascript
fetch('http://localhost:8080/companys/async', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    name: 'My Company',
    email: 'mycompany@example.com'
  })
})
.then(res => res.json())
.then(data => console.log('Created:', data));
```

### Using Java HttpClient
```java
HttpRequest req = HttpRequest.newBuilder(
    URI.create("http://localhost:8080/companys/async"))
    .POST(HttpRequest.BodyPublishers.ofString(json))
    .header("Content-Type", "application/json")
    .build();

httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString())
    .thenApply(HttpResponse::body)
    .thenAccept(System.out::println);
```

---

## 📞 Support

For more information, see:
- **VERIFICATION_COMPLETE.md** - Complete verification report
- **ENDPOINT_VERIFICATION_REPORT.md** - Detailed endpoint specs
- **HTTP_ENDPOINT_VERIFICATION_SUMMARY.md** - Testing procedures

---

**Status: ✅ All Endpoints Operational**
**Verified: January 10, 2026**
**Ready for: Angular, Scan Requests, Production**

