#!/bin/bash

# Spring Boot Server Test with Console Output
# This script starts the server and tests all endpoints with visible JSON responses

echo ""
echo "╔════════════════════════════════════════════════════════════════╗"
echo "║        Spring Boot REST API - Console Test Suite              ║"
echo "║              Running without Maven or Xcode                   ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

BASE_URL="http://localhost:8080"
RESULTS_FILE="/tmp/test_results.log"

# Clear previous results
> "$RESULTS_FILE"

# Function to test endpoint and display results
test_endpoint() {
    local test_num=$1
    local method=$2
    local endpoint=$3
    local data=$4
    local description=$5

    echo ""
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo "TEST $test_num: $description"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo "Method: $method"
    echo "URL: $BASE_URL$endpoint"

    if [ ! -z "$data" ]; then
        echo "Request Body:"
        echo "$data" | python3 -m json.tool 2>/dev/null || echo "$data"
    fi

    echo ""
    echo "RESPONSE:"
    echo "─────────────────────────────────────────────────────────────"

    if [ "$method" = "GET" ]; then
        RESPONSE=$(curl -s -w "\n%{http_code}" "$BASE_URL$endpoint")
    else
        RESPONSE=$(curl -s -w "\n%{http_code}" -X "$method" "$BASE_URL$endpoint" \
            -H "Content-Type: application/json" \
            -d "$data")
    fi

    # Extract status code (last line)
    STATUS=$(echo "$RESPONSE" | tail -n1)
    # Extract body (all except last line)
    BODY=$(echo "$RESPONSE" | sed '$d')

    echo "HTTP Status: $STATUS"
    echo ""
    echo "Response Body:"
    echo "$BODY" | python3 -m json.tool 2>/dev/null || echo "$BODY"
    echo ""

    if [ "$STATUS" -ge 200 ] && [ "$STATUS" -lt 300 ]; then
        echo "✓ SUCCESS"
    else
        echo "✗ FAILED"
    fi
    echo "─────────────────────────────────────────────────────────────"
}

# Kill any existing Java processes on port 8080
echo "Checking for existing processes on port 8080..."
lsof -i :8080 2>/dev/null | grep LISTEN | awk '{print $2}' | xargs kill -9 2>/dev/null
sleep 2

# Start the Spring Boot server
echo "Starting Spring Boot Application from JAR..."
echo "JAR: /Users/mjh5153/Downloads/demo/target/demo-0.0.1-SNAPSHOT.jar"
echo ""

cd /Users/mjh5153/Downloads/demo
java -jar target/demo-0.0.1-SNAPSHOT.jar > /tmp/server.log 2>&1 &
SERVER_PID=$!
echo "Server PID: $SERVER_PID"
echo "Waiting 15 seconds for server to start..."
echo ""

sleep 15

# Verify server is running
if ! curl -s "$BASE_URL/companys" > /dev/null 2>&1; then
    echo "✗ ERROR: Server failed to start"
    echo "Check logs with: tail -50 /tmp/server.log"
    exit 1
fi

echo "✓ Server is running!"
echo ""

# Run all tests
test_endpoint 1 "GET" "/companys" "" "Get All Companies"

test_endpoint 2 "GET" "/companys/company" "" "Get Single Company"

test_endpoint 3 "GET" "/companys/1/TestCompany" "" "Get Company by Path Variables"

test_endpoint 4 "GET" "/companys/query?id=5&name=QueryTest" "" "Get Company by Query Parameters"

test_endpoint 5 "POST" "/companys" \
    '{"id":null,"name":"Test Company","email":"test@example.com"}' \
    "Create Company (Sync)"

test_endpoint 6 "POST" "/companys/async" \
    '{"id":null,"name":"Async Test Company","email":"async@example.com"}' \
    "Create Company (Async)"

test_endpoint 7 "POST" "/companys/batch/async" \
    '[{"id":null,"name":"Batch Company 1","email":"batch1@example.com"},{"id":null,"name":"Batch Company 2","email":"batch2@example.com"},{"id":null,"name":"Batch Company 3","email":"batch3@example.com"}]' \
    "Batch Create Companies (Async)"

test_endpoint 8 "PUT" "/companys/1" \
    '{"id":1,"name":"Updated Company","email":"updated@example.com"}' \
    "Update Company (Sync)"

test_endpoint 9 "POST" "/api/comply/process" \
    '{"id":null,"name":"Comply Test Company","email":"comply@example.com"}' \
    "COMPLY API - Process Single"

test_endpoint 10 "POST" "/api/comply/process/batch" \
    '[{"id":null,"name":"Comply Batch 1","email":"comply1@example.com"},{"id":null,"name":"Comply Batch 2","email":"comply2@example.com"}]' \
    "COMPLY API - Process Batch"

# Summary
echo ""
echo "╔════════════════════════════════════════════════════════════════╗"
echo "║                     TEST SUITE COMPLETED                       ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""
echo "✓ All 10 Endpoints Tested"
echo "✓ JSON Responses Displayed"
echo "✓ Server Running on Port 8080"
echo ""
echo "Server PID: $SERVER_PID"
echo ""
echo "To stop the server, run:"
echo "  kill $SERVER_PID"
echo ""
echo "To view server logs, run:"
echo "  tail -f /tmp/server.log"
echo ""

