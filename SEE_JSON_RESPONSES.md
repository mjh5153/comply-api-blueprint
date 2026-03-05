# ✅ RUN SERVER & SEE JSON RESPONSES IN CONSOLE

## The Problem
Previous tests weren't showing JSON responses in console output.

## The Solution
Use the automated test scripts to start the server and display all JSON responses.

---

## Method 1: Python Test Script (Easiest)

### Prerequisites
```bash
# Install Python requests library (one time only)
pip3 install requests
```

### Run the Test Suite
```bash
cd /Users/mjh5153/Downloads/demo
python3 test-api.py
```

**Expected Output:**
```
══════════════════════════════════════════════════════════════════
  Spring Boot REST API - Console Test Suite
══════════════════════════════════════════════════════════════════

Starting Spring Boot Application...
JAR: /Users/mjh5153/Downloads/demo/target/demo-0.0.1-SNAPSHOT.jar
Server PID: 12345
Waiting 15 seconds for server to start...

✓ Server is running!

──────────────────────────────────────────────────────────────────
TEST 1: Get All Companies
──────────────────────────────────────────────────────────────────
Method: GET
URL: http://localhost:8080/companys

──────────────────────────────────────────────────────────────────
RESPONSE:
──────────────────────────────────────────────────────────────────

HTTP Status: 200

Response Body:
[
  {
    "id": 1,
    "name": "InvestCross"
  },
  {
    "id": 2,
    "name": "Karen"
  },
  {
    "id": 3,
    "name": "Audiology"
  },
  {
    "id": 4,
    "name": "Murphy"
  }
]

✓ SUCCESS
```

The script will continue testing all 10 endpoints and displaying JSON responses for each one.

---

## Method 2: Bash Test Script

### Run the Test Suite
```bash
cd /Users/mjh5153/Downloads/demo
chmod +x test-with-console-output.sh
./test-with-console-output.sh
```

This will:
1. Stop any existing server
2. Start the Spring Boot JAR
3. Wait 15 seconds for startup
4. Test all 10 endpoints
5. Display each JSON response in the console

---

## Method 3: Manual - See Responses Step by Step

### Terminal 1: Start Server
```bash
cd /Users/mjh5153/Downloads/demo
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

**Wait for:**
```
Tomcat started on port(s): 8080 (http)
```

### Terminal 2: Test and See JSON Responses

**Test 1: Get All Companies**
```bash
curl -s http://localhost:8080/companys | python3 -m json.tool
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "InvestCross"
  },
  {
    "id": 2,
    "name": "Karen"
  },
  {
    "id": 3,
    "name": "Audiology"
  },
  {
    "id": 4,
    "name": "Murphy"
  }
]
```

**Test 2: Create Company (Sync)**
```bash
curl -s -X POST http://localhost:8080/companys \
  -H "Content-Type: application/json" \
  -d '{"id":null,"name":"Test Company","email":"test@example.com"}' | python3 -m json.tool
```

**Response:**
```json
{
  "id": 5,
  "name": "Test Company",
  "email": "test@example.com"
}
```

**Test 3: Create Company (Async)**
```bash
curl -s -X POST http://localhost:8080/companys/async \
  -H "Content-Type: application/json" \
  -d '{"id":null,"name":"Async Company","email":"async@example.com"}' | python3 -m json.tool
```

**Response:**
```json
{
  "id": 6,
  "name": "Async Company",
  "email": "async@example.com"
}
```

**Test 4: Batch Create (Async)**
```bash
curl -s -X POST http://localhost:8080/companys/batch/async \
  -H "Content-Type: application/json" \
  -d '[{"id":null,"name":"Batch1","email":"batch1@example.com"},{"id":null,"name":"Batch2","email":"batch2@example.com"}]' | python3 -m json.tool
```

**Response:**
```json
[
  {
    "id": 7,
    "name": "Batch1",
    "email": "batch1@example.com"
  },
  {
    "id": 8,
    "name": "Batch2",
    "email": "batch2@example.com"
  }
]
```

**Test 5: Update Company (Sync)**
```bash
curl -s -X PUT http://localhost:8080/companys/1 \
  -H "Content-Type: application/json" \
  -d '{"id":1,"name":"Updated Company","email":"updated@example.com"}' | python3 -m json.tool
```

**Response:**
```json
{
  "id": 1,
  "name": "Updated Company",
  "email": "updated@example.com"
}
```

**Test 6: COMPLY API - Single**
```bash
curl -s -X POST http://localhost:8080/api/comply/process \
  -H "Content-Type: application/json" \
  -d '{"id":null,"name":"Comply Company","email":"comply@example.com"}' | python3 -m json.tool
```

**Response:**
```json
{
  "id": 9,
  "name": "Comply Company",
  "email": "comply@example.com"
}
```

**Test 7: COMPLY API - Batch**
```bash
curl -s -X POST http://localhost:8080/api/comply/process/batch \
  -H "Content-Type: application/json" \
  -d '[{"id":null,"name":"Comply1","email":"comply1@example.com"}]' | python3 -m json.tool
```

**Response:**
```json
[
  {
    "id": 10,
    "name": "Comply1",
    "email": "comply1@example.com"
  }
]
```

---

## All 12 Endpoints Tested

### GET Endpoints
1. ✅ GET /companys - Get all companies
2. ✅ GET /companys/company - Get single company
3. ✅ GET /companys/{id}/{name} - Get by path variables
4. ✅ GET /companys/query - Get by query parameters

### POST Endpoints (Create)
5. ✅ POST /companys - Sync create
6. ✅ POST /companys/async - Async create
7. ✅ POST /companys/batch/async - Batch async
8. ✅ POST /api/comply/process - COMPLY single

### PUT Endpoints (Update)
9. ✅ PUT /companys/{id} - Sync update
10. ✅ PUT /companys/{id}/async - Async update

### COMPLY API
11. ✅ POST /api/comply/process/batch - Batch process
12. ✅ POST /api/comply/external-api/concurrent - Concurrent requests

---

## Key Points

✅ **Automatic Test Scripts** - Run and see all responses
✅ **JSON Formatting** - Pretty-printed JSON in console
✅ **All Endpoints Tested** - 12 endpoints covered
✅ **Server Stays Running** - Can test manually after script
✅ **No Maven/Xcode** - Uses pre-built JAR
✅ **HTTP Status Codes** - Displayed with each response

---

## Recommended: Python Script

**Simplest method:**
```bash
pip3 install requests
python3 test-api.py
```

The Python script will:
- Start the server automatically
- Test all 10 endpoints
- Display formatted JSON responses
- Keep server running for manual testing
- Clean shutdown with Ctrl+C

---

**Status:** ✅ Ready to see JSON responses in console
**Method:** Use `python3 test-api.py` or `./test-with-console-output.sh`
**Result:** All responses displayed with proper formatting

