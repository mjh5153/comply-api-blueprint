# ✅ See JSON Responses - Simple Method (No Python Dependencies)

## The Easiest Way - Just Use curl

### Step 1: Start the Server (Terminal 1)
```bash
cd /Users/mjh5153/Downloads/demo
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

Wait for:
```
Tomcat started on port(s): 8080 (http)
```

---

### Step 2: Test Endpoints (Terminal 2 - After 10 seconds)

#### Test 1: Get All Companies
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

---

#### Test 2: Get Single Company
```bash
curl -s http://localhost:8080/companys/company | python3 -m json.tool
```

**Response:**
```json
{
  "id": 1,
  "name": "CompanyTest"
}
```

---

#### Test 3: Create Company (Sync)
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

---

#### Test 4: Create Company (Async)
```bash
curl -s -X POST http://localhost:8080/companys/async \
  -H "Content-Type: application/json" \
  -d '{"id":null,"name":"Async Test Company","email":"async@example.com"}' | python3 -m json.tool
```

**Response:**
```json
{
  "id": 6,
  "name": "Async Test Company",
  "email": "async@example.com"
}
```

---

#### Test 5: Batch Create (Async)
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

---

#### Test 6: Update Company (Sync)
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

---

#### Test 7: Get by Path Variables
```bash
curl -s http://localhost:8080/companys/1/TestCompany | python3 -m json.tool
```

**Response:**
```json
{
  "id": 1,
  "name": "TestCompany"
}
```

---

#### Test 8: Get by Query Parameters
```bash
curl -s "http://localhost:8080/companys/query?id=5&name=QueryTest" | python3 -m json.tool
```

**Response:**
```json
{
  "id": 5,
  "name": "QueryTest"
}
```

---

#### Test 9: COMPLY API - Single Process
```bash
curl -s -X POST http://localhost:8080/api/comply/process \
  -H "Content-Type: application/json" \
  -d '{"id":null,"name":"Comply Test Company","email":"comply@example.com"}' | python3 -m json.tool
```

**Response:**
```json
{
  "id": 9,
  "name": "Comply Test Company",
  "email": "comply@example.com"
}
```

---

#### Test 10: COMPLY API - Batch Process
```bash
curl -s -X POST http://localhost:8080/api/comply/process/batch \
  -H "Content-Type: application/json" \
  -d '[{"id":null,"name":"Comply1","email":"comply1@example.com"},{"id":null,"name":"Comply2","email":"comply2@example.com"}]' | python3 -m json.tool
```

**Response:**
```json
[
  {
    "id": 10,
    "name": "Comply1",
    "email": "comply1@example.com"
  },
  {
    "id": 11,
    "name": "Comply2",
    "email": "comply2@example.com"
  }
]
```

---

## All 10 Endpoints Tested ✅

1. ✅ GET /companys - Get All Companies
2. ✅ GET /companys/company - Get Single Company
3. ✅ GET /companys/{id}/{name} - Path Variables
4. ✅ GET /companys/query - Query Parameters
5. ✅ POST /companys - Create (Sync)
6. ✅ POST /companys/async - Create (Async)
7. ✅ POST /companys/batch/async - Batch Create (Async)
8. ✅ PUT /companys/{id} - Update (Sync)
9. ✅ POST /api/comply/process - COMPLY Single
10. ✅ POST /api/comply/process/batch - COMPLY Batch

---

## Run All Tests at Once

Use the bash script:
```bash
cd /Users/mjh5153/Downloads/demo
chmod +x run-tests-final.sh
./run-tests-final.sh
```

Or the Python script (no dependencies):
```bash
cd /Users/mjh5153/Downloads/demo
python3 test-api-no-deps.py
```

---

## Summary

✅ **Start Server:** `java -jar target/demo-0.0.1-SNAPSHOT.jar`
✅ **Test Endpoint:** `curl -s http://localhost:8080/companys | python3 -m json.tool`
✅ **See JSON:** All responses displayed in console
✅ **All Endpoints:** Working and tested
✅ **No Dependencies:** Just curl and Python (both built-in on macOS)

---

**Status:** Ready to run - just follow the commands above!

