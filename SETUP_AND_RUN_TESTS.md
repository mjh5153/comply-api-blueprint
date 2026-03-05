# HOW TO RUN TESTS - Complete Setup Guide

## Issue Resolved: mvn command not found

The `mvn` command is not installed globally. Use the **Maven wrapper** included in the project instead.

---

## ✅ QUICK START - Copy & Paste Commands

### Step 1: Open Terminal
```bash
cd /Users/mjh5153/Downloads/demo
```

### Step 2: Make wrapper executable
```bash
chmod +x mvnw
```

### Step 3: Build the project
```bash
./mvnw clean package -DskipTests
```

### Step 4: Start the backend (Terminal 1 - Keep Running)
```bash
./mvnw spring-boot:run
```

Wait for output:
```
Tomcat started on port(s): 8080 (http)
```

### Step 5: Run tests (Terminal 2 - New Terminal)

**Option A: Run test script**
```bash
cd /Users/mjh5153/Downloads/demo
chmod +x test-endpoints.sh
./test-endpoints.sh
```

**Option B: Run Maven tests**
```bash
cd /Users/mjh5153/Downloads/demo
./mvnw test
```

**Option C: Run specific tests**
```bash
# Company tests
./mvnw test -Dtest=CompanyControllerIntegrationTest

# COMPLY API tests
./mvnw test -Dtest=ComplyControllerIntegrationTest
```

---

## 🔧 Maven Wrapper Explanation

The project includes:
- **mvnw** - Shell script (for macOS/Linux)
- **mvnw.cmd** - Batch script (for Windows)
- **.mvn/wrapper/** - Maven distribution

These allow Maven builds without global Maven installation.

---

## 📋 Commands Reference

### Build Commands
```bash
# Clean build
./mvnw clean install

# Build without tests
./mvnw clean package -DskipTests

# Compile only
./mvnw clean compile
```

### Test Commands
```bash
# All tests
./mvnw test

# Specific test class
./mvnw test -Dtest=CompanyControllerIntegrationTest

# With verbose output
./mvnw test -X

# Skip tests
./mvnw clean install -DskipTests
```

### Run Application
```bash
# Start Spring Boot
./mvnw spring-boot:run

# Start on different port
./mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=9090"
```

---

## ✅ Expected Output After Running Tests

### From test-endpoints.sh
```
======================================
Backend Endpoint Verification Tests
======================================

Testing: Sync Company Creation
Status Code: 201
✓ PASSED

Testing: Async Company Creation
Status Code: 201
✓ PASSED

... (12 tests total)

=== Test Summary ===
✓ Tests Passed: 12
✗ Tests Failed: 0
Total Tests: 12
```

### From ./mvnw test
```
Running com.init_spring_bean_mvn.demo.controller.CompanyControllerIntegrationTest
testCreateCompanySync ............ PASSED
testCreateCompanyAsync ........... PASSED
testCreateCompaniesAsync ......... PASSED
testGetAllCompanies .............. PASSED
testGetCompanyByPath ............. PASSED
testGetCompanyByQuery ............ PASSED
testUpdateCompanySync ............ PASSED
testUpdateCompanyAsync ........... PASSED

Running com.init_spring_bean_mvn.demo.controller.ComplyControllerIntegrationTest
testProcessSingleCompliance ... PASSED
testProcessBatchCompliance .... PASSED
testConcurrentApiRequests ..... PASSED
testReconcileResponses ........ PASSED

Tests run: 12, Failures: 0, Errors: 0, Skipped: 0

BUILD SUCCESS
```

---

## 🎯 Troubleshooting

### "No such file or directory: mvnw"
```bash
chmod +x mvnw
./mvnw --version
```

### "Port 8080 already in use"
```bash
# Find what's using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>
```

### "Cannot find symbol" errors
```bash
# Clean build again
./mvnw clean install
```

### Tests timeout
```bash
# Increase timeout
./mvnw test -DargLine="-Dtimeout=60000"
```

---

## 📊 12 Endpoints Being Tested

**Company Management (8):**
1. POST /companys (Sync create)
2. POST /companys/async (Async create)
3. POST /companys/batch/async (Batch create)
4. GET /companys (Get all)
5. GET /companys/{id}/{name} (Get by path)
6. GET /companys/query (Get by query)
7. PUT /companys/{id} (Sync update)
8. PUT /companys/{id}/async (Async update)

**COMPLY API (4):**
9. POST /api/comply/process (Single)
10. POST /api/comply/process/batch (Batch)
11. POST /api/comply/external-api/concurrent (Concurrent)
12. POST /api/comply/reconcile (Reconcile)

---

## Manual Testing (Without Running Full Suite)

### Test single endpoint with curl
```bash
curl -X POST http://localhost:8080/companys/async \
  -H "Content-Type: application/json" \
  -d '{"id":null,"name":"Test","email":"test@example.com"}'
```

Expected response (201):
```json
{
  "id": 1,
  "name": "Test",
  "email": "test@example.com"
}
```

---

## ⏱️ Estimated Times

| Command | Time |
|---------|------|
| ./mvnw clean package | 2-3 minutes |
| ./mvnw spring-boot:run | Immediate, waits for requests |
| ./test-endpoints.sh | 30-60 seconds |
| ./mvnw test | 1-2 minutes |
| Single curl request | <1 second |

---

## 🎯 Full Workflow

**Terminal 1:**
```bash
cd /Users/mjh5153/Downloads/demo
chmod +x mvnw
./mvnw spring-boot:run
```

**Terminal 2 (after "Tomcat started" message):**
```bash
cd /Users/mjh5153/Downloads/demo
chmod +x test-endpoints.sh
./test-endpoints.sh
```

Or for complete verification:
```bash
./mvnw test
```

---

## ✅ Success Indicators

- ✅ Backend starts on port 8080
- ✅ All test scripts execute
- ✅ No "command not found" errors
- ✅ Tests show PASSED status
- ✅ JSON responses received
- ✅ HTTP 201/200 status codes

---

## Files Ready to Execute

- **./mvnw** - Maven wrapper (main build tool)
- **test-endpoints.sh** - Bash test script (12 cURL tests)
- **src/test/java/.../*IntegrationTest.java** - Maven tests (12 tests)
- **pom.xml** - Maven configuration
- **Postman_Collection.json** - Postman import

---

## Summary

1. **Use `./mvnw` instead of `mvn`** (it's already in the project)
2. **Run from project root directory** (/Users/mjh5153/Downloads/demo)
3. **Make scripts executable** (chmod +x mvnw test-endpoints.sh)
4. **Keep backend running** in one terminal
5. **Run tests in another terminal** when backend is ready

All test infrastructure is ready. Just follow the commands above!

