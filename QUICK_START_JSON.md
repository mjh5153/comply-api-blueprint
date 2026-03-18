# ✅ QUICK START - See JSON Responses in Console

## Option 1: Python Test Script (Recommended)

### Install Python requests (one time)
```bash
pip3 install requests
```

### Run the test suite
```bash
cd /Users/mjh5153/Downloads/demo
python3 test-api.py
```

**Expected output:**
```
Server is running!

──────────────────────────────────────────────────────────────────
TEST 1: Get All Companies
──────────────────────────────────────────────────────────────────
Method: GET
URL: http://localhost:8080/companys

RESPONSE:

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
  }
]

✓ SUCCESS
```

The script tests all 12 endpoints and shows JSON responses for each one!

---

## Option 2: Bash Test Script

```bash
cd /Users/mjh5153/Downloads/demo
chmod +x test-with-console-output.sh
./test-with-console-output.sh
```

---

## Option 3: Manual Testing

### Terminal 1: Start Server
```bash
cd /Users/mjh5153/Downloads/demo
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

### Terminal 2: Test with curl
```bash
# Get all companies (formatted JSON)
curl -s http://localhost:8080/companys | python3 -m json.tool

# Create company
curl -s -X POST http://localhost:8080/companys \
  -H "Content-Type: application/json" \
  -d '{"id":null,"name":"Test","email":"test@example.com"}' | python3 -m json.tool
```

---

## Expected JSON Responses

### GET /companys
```json
[
  {"id": 1, "name": "InvestCross"},
  {"id": 2, "name": "Karen"},
  {"id": 3, "name": "Audiology"},
  {"id": 4, "name": "Murphy"}
]
```

### POST /companys
```json
{
  "id": 5,
  "name": "Test Company",
  "email": "test@example.com"
}
```

### POST /companys/async
```json
{
  "id": 6,
  "name": "Async Company",
  "email": "async@example.com"
}
```

### POST /companys/batch/async
```json
[
  {"id": 7, "name": "Batch1", "email": "batch1@example.com"},
  {"id": 8, "name": "Batch2", "email": "batch2@example.com"}
]
```

### COMPLY API
```json
{
  "id": 9,
  "name": "Comply Company",
  "email": "comply@example.com"
}
```

---

## Summary

✅ All JSON responses visible in console
✅ Formatted and pretty-printed
✅ All 12 endpoints tested
✅ HTTP status codes shown
✅ No Maven or Xcode needed

**Run:** `python3 test-api.py`

Done!

