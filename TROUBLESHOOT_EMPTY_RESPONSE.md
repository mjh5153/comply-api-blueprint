# ✅ TROUBLESHOOTING - Empty Response from curl

## Problem
```
curl -s http://localhost:8080/companys | python3 -m json.tool
Expecting value: line 1 column 1 (char 0)
```

**Root Cause:** Empty response from server = **Server is not running or not responding**

---

## ✅ Solution Steps

### Step 1: Kill All Existing Java Processes
Open a terminal and run:
```bash
pkill -f java
sleep 3
```

This ensures no lingering processes are blocking port 8080.

---

### Step 2: Start Fresh - Run Server with Output
Open **Terminal 1** and run:
```bash
cd /Users/mjh5153/Downloads/demo
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

**WAIT and look for these messages in Terminal 1:**
```
...
2026-01-10 ... Starting DemoApplication v0.0.1-SNAPSHOT
2026-01-10 ... The following profiles are active:
2026-01-10 ... Tomcat started on port(s): 8080 (http)
2026-01-10 ... Started DemoApplication in X.XXX seconds
```

**This means the server is running successfully!**

---

### Step 3: Test in New Terminal
Open **Terminal 2** and run:
```bash
# Test without JSON formatting first
curl -v http://localhost:8080/companys

# If that works, try with JSON formatting
curl -s http://localhost:8080/companys | python3 -m json.tool
```

---

## Expected Output

### Raw Response (curl -v)
```
...
> GET /companys HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.x.x
...
< HTTP/1.1 200 
< Content-Type: application/json
< Content-Length: 123
<
[
  {
    "id": 1,
    "name": "InvestCross"
  },
  {
    "id": 2,
    "name": "Karen"
  }
]
```

### JSON Formatted (curl -s ... | python3 -m json.tool)
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

## If Still Getting Empty Response

### Check 1: Is server actually running?
```bash
# In another terminal, check if Java is running
ps aux | grep java | grep demo

# Should show something like:
# java -jar target/demo-0.0.1-SNAPSHOT.jar
```

### Check 2: Is port 8080 in use?
```bash
# Check what's on port 8080
lsof -i :8080

# Should show Java listening
```

### Check 3: Try connecting with simple test
```bash
# Simple connection test
telnet localhost 8080

# Or use nc
nc -zv localhost 8080
```

---

## Common Issues & Fixes

### Issue 1: "Connection refused"
**Solution:** Server is not running
```bash
# Make sure server is started in Terminal 1 with:
java -jar target/demo-0.0.1-SNAPSHOT.jar

# Wait 20 seconds for startup
```

### Issue 2: "Empty response" or "No response"
**Solution:** Server crashed or didn't start
```bash
# Check Terminal 1 for error messages
# Look for: ERROR or Exception

# Rebuild JAR if needed
./mvnw clean package -DskipTests

# Try again
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

### Issue 3: "Port 8080 already in use"
**Solution:** Kill existing process
```bash
# Kill all Java processes
pkill -f java
sleep 3

# Try again
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

---

## Complete Fresh Start Procedure

**If nothing is working, do this:**

```bash
# Terminal 1: Clean everything
cd /Users/mjh5153/Downloads/demo
pkill -f java
sleep 3

# Start fresh
java -jar target/demo-0.0.1-SNAPSHOT.jar

# Wait 20 seconds and look for "Tomcat started on port(s): 8080"
```

```bash
# Terminal 2: Test (after 20 seconds)
curl -v http://localhost:8080/companys

# You should see:
# < HTTP/1.1 200 
# [json response]
```

---

## Detailed Server Startup Sequence

The server goes through these steps:

1. **JVM starts** - Java Virtual Machine initializes
2. **Spring Boot starts** - Spring framework loads
3. **Auto-configuration** - Spring detects Spring Web, Data JPA, etc.
4. **Dependency Injection** - Beans created and wired
5. **Controllers registered** - CompanyController mapped to /companys
6. **Tomcat starts** - Embedded web server starts
7. **Listens on 8080** - Ready to accept requests
8. **Message printed** - "Tomcat started on port(s): 8080"

**If you don't see message 8, server didn't start successfully.**

---

## Manual Verification Steps

### Step 1: Check if JAR exists
```bash
ls -lh /Users/mjh5153/Downloads/demo/target/demo-0.0.1-SNAPSHOT.jar

# Should show ~11MB file
```

### Step 2: Check if Java is available
```bash
java -version

# Should show Java 17 or later
```

### Step 3: Run server with full output
```bash
cd /Users/mjh5153/Downloads/demo
java -jar target/demo-0.0.1-SNAPSHOT.jar 2>&1

# Watch for any ERROR messages
```

### Step 4: Once server is running, test
```bash
# In another terminal
curl http://localhost:8080/companys

# Should get JSON array response
```

---

## All Test Endpoints (Once Server Is Running)

```bash
# Get all companies
curl -s http://localhost:8080/companys | python3 -m json.tool

# Get single company
curl -s http://localhost:8080/companys/company | python3 -m json.tool

# Create company
curl -s -X POST http://localhost:8080/companys \
  -H "Content-Type: application/json" \
  -d '{"id":null,"name":"Test","email":"test@example.com"}' | python3 -m json.tool

# Async create
curl -s -X POST http://localhost:8080/companys/async \
  -H "Content-Type: application/json" \
  -d '{"id":null,"name":"Async","email":"async@example.com"}' | python3 -m json.tool

# Batch create
curl -s -X POST http://localhost:8080/companys/batch/async \
  -H "Content-Type: application/json" \
  -d '[{"id":null,"name":"B1","email":"b1@example.com"}]' | python3 -m json.tool

# COMPLY API
curl -s -X POST http://localhost:8080/api/comply/process \
  -H "Content-Type: application/json" \
  -d '{"id":null,"name":"Comply","email":"comply@example.com"}' | python3 -m json.tool
```

---

## Summary

| Problem | Solution |
|---------|----------|
| Empty response | Start server: `java -jar target/demo-0.0.1-SNAPSHOT.jar` |
| Connection refused | Wait 20 seconds for server to start |
| Port already in use | `pkill -f java` then start server |
| No output in Terminal 1 | Server may have crashed - check for ERROR messages |
| JSON parse error | Server returned empty response - check if running |

---

## Quick Reference

```bash
# Kill everything
pkill -f java

# Wait
sleep 3

# Start server (Terminal 1)
cd /Users/mjh5153/Downloads/demo && java -jar target/demo-0.0.1-SNAPSHOT.jar

# Test (Terminal 2 - after 20 seconds)
curl -s http://localhost:8080/companys | python3 -m json.tool
```

**This should work!** If it doesn't, check Terminal 1 for error messages.

---

**Status:** Troubleshooting guide complete  
**Next action:** Follow steps 1-3 above manually in your terminal  
**Expected result:** JSON array response from /companys endpoint

