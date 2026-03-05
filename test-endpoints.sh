#!/bin/bash

# HTTP Client Test Script for Backend Endpoints
# This script tests all created endpoints using cURL
# Requires: curl, jq (optional for JSON formatting)

BASE_URL="http://localhost:8080"
TIMESTAMP=$(date +%s%N)

echo "======================================"
echo "Backend Endpoint Verification Tests"
echo "======================================"
echo "Base URL: $BASE_URL"
echo "Timestamp: $TIMESTAMP"
echo ""

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Test counter
TESTS_PASSED=0
TESTS_FAILED=0

# Helper function to test endpoint
test_endpoint() {
    local name=$1
    local method=$2
    local endpoint=$3
    local data=$4
    local expected_status=$5

    echo -e "${YELLOW}Testing:${NC} $name"
    echo "Method: $method | Endpoint: $endpoint"

    if [ -z "$data" ]; then
        response=$(curl -s -w "\n%{http_code}" -X $method "$BASE_URL$endpoint" \
            -H "Content-Type: application/json")
    else
        response=$(curl -s -w "\n%{http_code}" -X $method "$BASE_URL$endpoint" \
            -H "Content-Type: application/json" \
            -d "$data")
    fi

    # Extract status code (last line)
    status_code=$(echo "$response" | tail -n1)
    # Extract response body (all lines except last)
    body=$(echo "$response" | sed '$d')

    echo "Status Code: $status_code"
    echo "Response: $body"

    if [ "$status_code" -eq "$expected_status" ]; then
        echo -e "${GREEN}✓ PASSED${NC}\n"
        ((TESTS_PASSED++))
    else
        echo -e "${RED}✗ FAILED${NC} (Expected: $expected_status)\n"
        ((TESTS_FAILED++))
    fi
}

# ============== Company Endpoints ==============
echo -e "\n${YELLOW}=== Company Endpoints ===${NC}\n"

# Test 1: Sync Company Creation
test_endpoint \
    "Sync Company Creation" \
    "POST" \
    "/companys" \
    '{"id":null,"name":"Test Company Sync","email":"sync@test.com"}' \
    "201"

# Test 2: Async Company Creation
test_endpoint \
    "Async Company Creation" \
    "POST" \
    "/companys/async" \
    '{"id":null,"name":"Test Company Async","email":"async@test.com"}' \
    "201"

# Test 3: Batch Async Company Creation
test_endpoint \
    "Batch Async Company Creation" \
    "POST" \
    "/companys/batch/async" \
    '[{"id":null,"name":"Batch 1","email":"batch1@test.com"},{"id":null,"name":"Batch 2","email":"batch2@test.com"}]' \
    "201"

# Test 4: Get All Companies
test_endpoint \
    "Get All Companies" \
    "GET" \
    "/companys" \
    "" \
    "200"

# Test 5: Get Company by Path Variables
test_endpoint \
    "Get Company by Path Variables" \
    "GET" \
    "/companys/1/TestCompany" \
    "" \
    "200"

# Test 6: Get Company by Query Parameters
test_endpoint \
    "Get Company by Query Parameters" \
    "GET" \
    "/companys/query?id=5&name=QueryTest" \
    "" \
    "200"

# Test 7: Update Company (Sync)
test_endpoint \
    "Update Company Sync" \
    "PUT" \
    "/companys/1" \
    '{"id":1,"name":"Updated Name","email":"updated@test.com"}' \
    "200"

# Test 8: Update Company (Async)
test_endpoint \
    "Update Company Async" \
    "PUT" \
    "/companys/1/async" \
    '{"id":1,"name":"Updated Async","email":"updatedasync@test.com"}' \
    "200"

# ============== COMPLY API Endpoints ==============
echo -e "\n${YELLOW}=== COMPLY API Endpoints ===${NC}\n"

# Test 9: Process Single Compliance
test_endpoint \
    "Process Single Compliance" \
    "POST" \
    "/api/comply/process" \
    '{"id":null,"name":"Comply Company","email":"comply@test.com"}' \
    "201"

# Test 10: Process Batch Compliance
test_endpoint \
    "Process Batch Compliance" \
    "POST" \
    "/api/comply/process/batch" \
    '[{"id":null,"name":"Comply Batch 1","email":"comply1@test.com"},{"id":null,"name":"Comply Batch 2","email":"comply2@test.com"}]' \
    "201"

# Test 11: Concurrent API Requests
test_endpoint \
    "Concurrent API Requests" \
    "POST" \
    "/api/comply/external-api/concurrent?apiEndpoint=http%3A%2F%2Flocalhost%3A8080" \
    '{"product":"apples","amount":"500","company":"InvestCross"}' \
    "200"

# Test 12: Reconcile Responses
test_endpoint \
    "Reconcile API Responses" \
    "POST" \
    "/api/comply/reconcile" \
    '["response1","response2","response3"]' \
    "200"

# ============== Summary ==============
echo -e "\n${YELLOW}=== Test Summary ===${NC}"
echo -e "${GREEN}Tests Passed: $TESTS_PASSED${NC}"
echo -e "${RED}Tests Failed: $TESTS_FAILED${NC}"
TOTAL=$((TESTS_PASSED + TESTS_FAILED))
echo "Total Tests: $TOTAL"

if [ $TESTS_FAILED -eq 0 ]; then
    echo -e "\n${GREEN}✓ All tests passed!${NC}"
    exit 0
else
    echo -e "\n${RED}✗ Some tests failed!${NC}"
    exit 1
fi

