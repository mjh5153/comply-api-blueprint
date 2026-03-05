# ✅ QUICK FIX - Port 8080 Conflict

## The Problem
Port 8080 is already in use. Both OrderFulfillmentServer and Spring Boot want to use it.

## The Solution (3 Simple Steps)

### Step 1: Kill existing processes
```bash
pkill -f java
sleep 2
```

### Step 2: Start Spring Boot
```bash
cd /Users/mjh5153/Downloads/demo
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

### Step 3: Test in new terminal (wait 10 seconds first)
```bash
curl -s http://localhost:8080/companys | python3 -m json.tool
```

**You'll see:**
```json
[
  {"id": 1, "name": "InvestCross"},
  {"id": 2, "name": "Karen"},
  {"id": 3, "name": "Audiology"},
  {"id": 4, "name": "Murphy"}
]
```

---

## Or Use the Smart Script

```bash
cd /Users/mjh5153/Downloads/demo
chmod +x start-server.sh
./start-server.sh
```

This script:
- ✅ Kills existing processes automatically
- ✅ Starts the server
- ✅ Verifies it's running
- ✅ Shows test commands

---

## All Endpoints Then Work

✅ GET /companys
✅ POST /companys
✅ POST /companys/async
✅ POST /companys/batch/async
✅ PUT /companys/{id}
✅ Plus COMPLY API endpoints

---

**Run:** `pkill -f java && sleep 2 && cd /Users/mjh5153/Downloads/demo && java -jar target/demo-0.0.1-SNAPSHOT.jar`

Done!

