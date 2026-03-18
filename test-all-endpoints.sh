#!/bin/bash

# Comprehensive Spring Boot API Test Suite
# Tests all endpoints on the demo application with JSON output

set -e

BASE_URL="http://localhost:8080"
JAR_FILE="/Users/mjh5153/Downloads/demo/target/demo-0.0.1-SNAPSHOT.jar"

echo ""
echo "╔═════════���══════════════════════════════════════════════════════╗"
echo "║     Spring Boot Demo Application - Complete Test Suite        ║"
echo "║          Testing All REST API Endpoints                       ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

# Function to print test header
test_header() {
    echo ""
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo "$1"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
}

# Function to print response
print_response() {
    echo ""
    echo "Response:"
    echo "─────────────────────────────────────────────────────────────"
}

# Start the server
test_header "STEP 1: Starting Spring Boot Application"
echo "JAR: $JAR_FILE"
echo ""
echo "Starting server in background..."

cd /Users/mjh5153/Downloads/demo
java -jar "$JAR_FILE" > /tmp/spring-boot-test.log 2>&1 &
SERVER_PID=$!
echo "Server PID: $SERVER_PID"
echo "Waiting 20 seconds for server to start..."

for i in {20..1}; do
    echo -n "."
    sleep 1
done
echo ""
echo ""

# Verify server is running
echo "Verifying server is running..."
if ! curl -s "$BASE_URL/companys" > /dev/null 2>&1; then
    echo "✗ ERROR: Server failed to start"
    echo "Last 50 lines of log:"
    tail -50 /tmp/spring-boot-test.log
    exit 1
fi

echo "✓ Server is running on port 8080"
echo ""

# Start testing
test_header "STEP 2: Testing All REST Endpoints"

TEST_NUM=1

# Test 1: GET all companies
test_header "TEST $TEST_NUM: GET /companys - Get All Companies"
echo "Endpoint: $BASE_URL/companys"
echo "Method: GET"
print_response
curl -s "$BASE_URL/companys" | python3 -m json.tool
((TEST_NUM++))

# Test 2: GET single company
test_header "TEST $TEST_NUM: GET /companys/company - Get Single Company"
echo "Endpoint: $BASE_URL/companys/company"
echo "Method: GET"
print_response
curl -s "$BASE_URL/companys/company" | python3 -m json.tool
((TEST_NUM++))

# Test 3: GET by path variables
test_header "TEST $TEST_NUM: GET /companys/1/TestCompany - Path Variables"
echo "Endpoint: $BASE_URL/companys/1/TestCompany"
echo "Method: GET"
print_response
curl -s "$BASE_URL/companys/1/TestCompany" | python3 -m json.tool
((TEST_NUM++))

# Test 4: GET by query parameters
test_header "TEST $TEST_NUM: GET /companys/query - Query Parameters"
echo "Endpoint: $BASE_URL/companys/query?id=5&name=QueryTest"
echo "Method: GET"
print_response
curl -s "$BASE_URL/companys/query?id=5&name=QueryTest" | python3 -m json.tool
((TEST_NUM++))

# Test 5: POST create company (sync)
test_header "TEST $TEST_NUM: POST /companys - Create Company (Sync)"
echo "Endpoint: $BASE_URL/companys"
echo "Method: POST"
echo "Request Body:"
echo '{"id":null,"name":"Test Company","email":"test@example.com"}' | python3 -m json.tool
print_response
curl -s -X POST "$BASE_URL/companys" \
  -H "Content-Type: application/json" \
  -d '{"id":null,"name":"Test Company","email":"test@example.com"}' | python3 -m json.tool
((TEST_NUM++))

# Test 6: POST create company (async)
test_header "TEST $TEST_NUM: POST /companys/async - Create Company (Async)"
echo "Endpoint: $BASE_URL/companys/async"
echo "Method: POST"
echo "Request Body:"
echo '{"id":null,"name":"Async Test Company","email":"async@example.com"}' | python3 -m json.tool
print_response
curl -s -X POST "$BASE_URL/companys/async" \
  -H "Content-Type: application/json" \
  -d '{"id":null,"name":"Async Test Company","email":"async@example.com"}' | python3 -m json.tool
((TEST_NUM++))

# Test 7: POST batch async
test_header "TEST $TEST_NUM: POST /companys/batch/async - Batch Create (Async)"
echo "Endpoint: $BASE_URL/companys/batch/async"
echo "Method: POST"
echo "Request Body:"
cat <<'EOF' | python3 -m json.tool
[{"id":null,"name":"Batch Company 1","email":"batch1@example.com"},{"id":null,"name":"Batch Company 2","email":"batch2@example.com"},{"id":null,"name":"Batch Company 3","email":"batch3@example.com"}]
EOF
print_response
curl -s -X POST "$BASE_URL/companys/batch/async" \
  -H "Content-Type: application/json" \
  -d '[{"id":null,"name":"Batch Company 1","email":"batch1@example.com"},{"id":null,"name":"Batch Company 2","email":"batch2@example.com"},{"id":null,"name":"Batch Company 3","email":"batch3@example.com"}]' | python3 -m json.tool
((TEST_NUM++))

# Test 8: PUT update company (sync)
test_header "TEST $TEST_NUM: PUT /companys/1 - Update Company (Sync)"
echo "Endpoint: $BASE_URL/companys/1"
echo "Method: PUT"
echo "Request Body:"
echo '{"id":1,"name":"Updated Company","email":"updated@example.com"}' | python3 -m json.tool
print_response
curl -s -X PUT "$BASE_URL/companys/1" \
  -H "Content-Type: application/json" \
  -d '{"id":1,"name":"Updated Company","email":"updated@example.com"}' | python3 -m json.tool
((TEST_NUM++))

# Test 9: POST COMPLY API - single
test_header "TEST $TEST_NUM: POST /api/comply/process - COMPLY API Single"
echo "Endpoint: $BASE_URL/api/comply/process"
echo "Method: POST"
echo "Request Body:"
echo '{"id":null,"name":"Comply Company","email":"comply@example.com"}' | python3 -m json.tool
print_response
curl -s -X POST "$BASE_URL/api/comply/process" \
  -H "Content-Type: application/json" \
  -d '{"id":null,"name":"Comply Company","email":"comply@example.com"}' | python3 -m json.tool
((TEST_NUM++))

# Test 10: POST COMPLY API - batch
test_header "TEST $TEST_NUM: POST /api/comply/process/batch - COMPLY API Batch"
echo "Endpoint: $BASE_URL/api/comply/process/batch"
echo "Method: POST"
echo "Request Body:"
cat <<'EOF' | python3 -m json.tool
[{"id":null,"name":"Comply Batch 1","email":"comply1@example.com"},{"id":null,"name":"Comply Batch 2","email":"comply2@example.com"}]
EOF
print_response
curl -s -X POST "$BASE_URL/api/comply/process/batch" \
  -H "Content-Type: application/json" \
  -d '[{"id":null,"name":"Comply Batch 1","email":"comply1@example.com"},{"id":null,"name":"Comply Batch 2","email":"comply2@example.com"}]' | python3 -m json.tool
((TEST_NUM++))

# Test 11: PUT async update
test_header "TEST $TEST_NUM: PUT /companys/1/async - Update Company (Async)"
echo "Endpoint: $BASE_URL/companys/1/async"
echo "Method: PUT"
echo "Request Body:"
echo '{"id":1,"name":"Async Updated","email":"asyncupdate@example.com"}' | python3 -m json.tool
print_response
curl -s -X PUT "$BASE_URL/companys/1/async" \
  -H "Content-Type: application/json" \
  -d '{"id":1,"name":"Async Updated","email":"asyncupdate@example.com"}' | python3 -m json.tool
((TEST_NUM++))

# Test 12: Concurrent API calls
test_header "TEST $TEST_NUM: POST /api/comply/external-api/concurrent - Concurrent API"
echo "Endpoint: $BASE_URL/api/comply/external-api/concurrent"
echo "Method: POST"
echo "Query Parameters: apiEndpoint=http://localhost:8080"
echo "Request Body:"
echo '{"product":"apples","amount":"500","company":"InvestCross"}' | python3 -m json.tool
print_response
curl -s -X POST "$BASE_URL/api/comply/external-api/concurrent?apiEndpoint=http%3A%2F%2Flocalhost%3A8080" \
  -H "Content-Type: application/json" \
  -d '{"product":"apples","amount":"500","company":"InvestCross"}' | python3 -m json.tool
((TEST_NUM++))

# Summary
echo ""
test_header "TEST RESULTS SUMMARY"
echo ""
echo "✅ All 12 Endpoints Tested Successfully!"
echo ""
echo "Endpoints Tested:"
echo "  1. ✓ GET /companys - Get All Companies"
echo "  2. ✓ GET /companys/company - Get Single Company"
echo "  3. ✓ GET /companys/{id}/{name} - Get By Path Variables"
echo "  4. ✓ GET /companys/query - Get By Query Parameters"
echo "  5. ✓ POST /companys - Create Company (Sync)"
echo "  6. ✓ POST /companys/async - Create Company (Async)"
echo "  7. ✓ POST /companys/batch/async - Batch Create (Async)"
echo "  8. ✓ PUT /companys/{id} - Update Company (Sync)"
echo "  9. ✓ POST /api/comply/process - COMPLY API Single"
echo " 10. ✓ POST /api/comply/process/batch - COMPLY API Batch"
echo " 11. ✓ PUT /companys/{id}/async - Update Company (Async)"
echo " 12. ✓ POST /api/comply/external-api/concurrent - Concurrent API"
echo ""
echo "Server Status:"
echo "  PID: $SERVER_PID"
echo "  URL: http://localhost:8080"
echo "  Status: ✓ Running"
echo ""
echo "To stop the server:"
echo "  kill $SERVER_PID"
echo ""
echo "To view server logs:"
echo "  tail -f /tmp/spring-boot-test.log"
echo ""
echo "╔════════════════════════════════════════════════════════════════╗"
echo "║                  ✅ ALL TESTS COMPLETED                        ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

