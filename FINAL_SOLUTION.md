# ✅ SOLUTION - See JSON Responses in Console

## Problem: JSON responses not showing in console

## Solution: Use curl with python3 json formatter (both built-in on macOS)

---

## 🎯 Quickest Way (2 Terminals)

### Terminal 1: Start Server
```bash
cd /Users/mjh5153/Downloads/demo
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

**Wait for:**
```
Tomcat started on port(s): 8080 (http)
```

---

### Terminal 2: Test and See JSON (After 10 seconds)

#### Test 1: Get All Companies
```bash
curl -s http://localhost:8080/companys | python3 -m json.tool
```

**See:**
```json
[
  {"id": 1, "name": "InvestCross"},
  {"id": 2, "name": "Karen"},
  {"id": 3, "name": "Audiology"},
  {"id": 4, "name": "Murphy"}
]
```

---

#### Test 2: Create Company
```bash
curl -s -X POST http://localhost:8080/companys \
  -H "Content-Type: application/json" \
  -d '{"id":null,"name":"Test","email":"test@example.com"}' | python3 -m json.tool
```

**See:**
```json
{
  "id": 5,
  "name": "Test",
  "email": "test@example.com"
}
```

---

#### Test 3: Create Async
```bash
curl -s -X POST http://localhost:8080/companys/async \
  -H "Content-Type: application/json" \
  -d '{"id":null,"name":"AsyncTest","email":"async@example.com"}' | python3 -m json.tool
```

---

#### Test 4: Batch Create
```bash
curl -s -X POST http://localhost:8080/companys/batch/async \
  -H "Content-Type: application/json" \
  -d '[{"id":null,"name":"B1","email":"b1@example.com"},{"id":null,"name":"B2","email":"b2@example.com"}]' | python3 -m json.tool
```

---

#### Test 5: COMPLY API
```bash
curl -s -X POST http://localhost:8080/api/comply/process \
  -H "Content-Type: application/json" \
  -d '{"id":null,"name":"Comply","email":"comply@example.com"}' | python3 -m json.tool
```

---

## 🤖 Or Run All Tests at Once

### Option 1: Bash Script
```bash
cd /Users/mjh5153/Downloads/demo
chmod +x run-tests-final.sh
./run-tests-final.sh
```

### Option 2: Python Script (No Dependencies)
```bash
cd /Users/mjh5153/Downloads/demo
python3 test-api-no-deps.py
```

---

## ✅ All 10 Endpoints

1. GET /companys - Get All
2. GET /companys/company - Get Single
3. GET /companys/{id}/{name} - Path Variables
4. GET /companys/query - Query Parameters
5. POST /companys - Create (Sync)
6. POST /companys/async - Create (Async)
7. POST /companys/batch/async - Batch (Async)
8. PUT /companys/{id} - Update (Sync)
9. POST /api/comply/process - COMPLY Single
10. POST /api/comply/process/batch - COMPLY Batch

---

## What You Need

✅ Java (to run JAR)
✅ curl (built-in on macOS)
✅ python3 (built-in on macOS)

❌ Maven - NOT needed
❌ Xcode - NOT needed
❌ pip3 install - NOT needed

---

## Summary

**Start:** `java -jar target/demo-0.0.1-SNAPSHOT.jar`
**Test:** `curl -s http://localhost:8080/companys | python3 -m json.tool`
**See:** JSON responses in console ✅

---

**Status:** Ready to run - all tools already on macOS!

