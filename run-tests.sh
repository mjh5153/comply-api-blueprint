#!/bin/bash

# Simple test to verify endpoints without external backend
# This test uses HTTP client functionality to test endpoints

BASE_URL="http://localhost:8080"
TEST_FILE="/tmp/endpoint_test_results.txt"

> "$TEST_FILE"  # Clear the file

echo "Starting endpoint tests..." | tee -a "$TEST_FILE"
echo "Base URL: $BASE_URL" | tee -a "$TEST_FILE"
echo "" | tee -a "$TEST_FILE"

# Test 1: Get all companies
echo "=== Test 1: GET /companys ===" | tee -a "$TEST_FILE"
RESPONSE=$(curl -s -w "\n%{http_code}" -X GET "$BASE_URL/companys" 2>&1)
STATUS=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | sed '$d')
echo "Status: $STATUS" | tee -a "$TEST_FILE"
echo "Response: $BODY" | tee -a "$TEST_FILE"
echo "" | tee -a "$TEST_FILE"

# Test 2: Create company (sync)
echo "=== Test 2: POST /companys (Sync) ===" | tee -a "$TEST_FILE"
RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/companys" \
  -H "Content-Type: application/json" \
  -d '{"id":null,"name":"Test Company","email":"test@example.com"}' 2>&1)
STATUS=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | sed '$d')
echo "Status: $STATUS" | tee -a "$TEST_FILE"
echo "Response: $BODY" | tee -a "$TEST_FILE"
echo "" | tee -a "$TEST_FILE"

# Test 3: Create company (async)
echo "=== Test 3: POST /companys/async (Async) ===" | tee -a "$TEST_FILE"
RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/companys/async" \
  -H "Content-Type: application/json" \
  -d '{"id":null,"name":"Async Test","email":"async@example.com"}' 2>&1)
STATUS=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | sed '$d')
echo "Status: $STATUS" | tee -a "$TEST_FILE"
echo "Response: $BODY" | tee -a "$TEST_FILE"
echo "" | tee -a "$TEST_FILE"

# Test 4: Batch async
echo "=== Test 4: POST /companys/batch/async ===" | tee -a "$TEST_FILE"
RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/companys/batch/async" \
  -H "Content-Type: application/json" \
  -d '[{"id":null,"name":"Batch1","email":"batch1@example.com"},{"id":null,"name":"Batch2","email":"batch2@example.com"}]' 2>&1)
STATUS=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | sed '$d')
echo "Status: $STATUS" | tee -a "$TEST_FILE"
echo "Response: $BODY" | tee -a "$TEST_FILE"
echo "" | tee -a "$TEST_FILE"

# Test 5: COMPLY API - Single
echo "=== Test 5: POST /api/comply/process ===" | tee -a "$TEST_FILE"
RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/api/comply/process" \
  -H "Content-Type: application/json" \
  -d '{"id":null,"name":"Comply Company","email":"comply@example.com"}' 2>&1)
STATUS=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | sed '$d')
echo "Status: $STATUS" | tee -a "$TEST_FILE"
echo "Response: $BODY" | tee -a "$TEST_FILE"
echo "" | tee -a "$TEST_FILE"

# Test 6: COMPLY API - Batch
echo "=== Test 6: POST /api/comply/process/batch ===" | tee -a "$TEST_FILE"
RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/api/comply/process/batch" \
  -H "Content-Type: application/json" \
  -d '[{"id":null,"name":"Comply1","email":"comply1@example.com"},{"id":null,"name":"Comply2","email":"comply2@example.com"}]' 2>&1)
STATUS=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | sed '$d')
echo "Status: $STATUS" | tee -a "$TEST_FILE"
echo "Response: $BODY" | tee -a "$TEST_FILE"
echo "" | tee -a "$TEST_FILE"

echo "Test results saved to: $TEST_FILE" | tee -a "$TEST_FILE"
cat "$TEST_FILE"

