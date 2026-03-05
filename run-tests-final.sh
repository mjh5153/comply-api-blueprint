#!/bin/bash

# Simple Bash Test Script - No Dependencies
# Uses only curl (built-in on macOS) to test all endpoints
# Displays JSON responses in console

BASE_URL="http://localhost:8080"

echo ""
echo "╔════════════════════════════════════════════════════════════════╗"
echo "║        Spring Boot REST API - Full Test Suite                 ║"
echo "║              JSON Responses Displayed                          ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

# Function to test endpoint
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
    echo "Endpoint: $BASE_URL$endpoint"

    if [ ! -z "$data" ]; then
        echo "Request Body:"
        echo "$data" | python3 -m json.tool 2>/dev/null || echo "$data"
    fi

    echo ""
    echo "RESPONSE:"
    echo "─────────────────────────────────────────────────────────────"

    if [ "$method" = "GET" ]; then
        curl -s -w "\nHTTP Status: %{http_code}\n" "$BASE_URL$endpoint" | python3 -m json.tool 2>/dev/null || curl -s "$BASE_URL$endpoint"
    else
        curl -s -w "\nHTTP Status: %{http_code}\n" -X "$method" "$BASE_URL$endpoint" \
            -H "Content-Type: application/json" \
            -d "$data" | python3 -m json.tool 2>/dev/null || echo "Error parsing JSON"
    fi

    echo ""
    echo "✓ Test Complete"
    echo ""
}

# Kill any existing Java processes
echo "Cleaning up existing processes..."
lsof -i :8080 2>/dev/null | grep LISTEN | awk '{print $2}' | xargs kill -9 2>/dev/null || true
sleep 2

# Start Spring Boot server
echo "Starting Spring Boot Application..."
echo "JAR: /Users/mjh5153/Downloads/demo/target/demo-0.0.1-SNAPSHOT.jar"
echo ""

cd /Users/mjh5153/Downloads/demo
java -jar target/demo-0.0.1-SNAPSHOT.jar > /tmp/server.log 2>&1 &
SERVER_PID=$!

echo "Server PID: $SERVER_PID"
echo "Waiting 15 seconds for server to start..."
echo ""

# Wait for server
sleep 15

# Check if server is running
if ! curl -s "$BASE_URL/companys" > /dev/null 2>&1; then
    echo "✗ ERROR: Server failed to start"
    echo "Check logs: tail -50 /tmp/server.log"
    exit 1
fi

echo "✓ Server is running on port 8080"
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
echo "║                    TEST SUITE COMPLETED                        ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""
echo "✓ All 10 Endpoints Tested"
echo "✓ JSON Responses Displayed"
echo "✓ Server Running (PID: $SERVER_PID)"
echo ""
echo "To stop the server:"
echo "  kill $SERVER_PID"
echo ""
echo "To view server logs:"
echo "  tail -f /tmp/server.log"
echo ""
echo "To test manually in new terminal:"
echo "  curl -s http://localhost:8080/companys | python3 -m json.tool"
echo ""

