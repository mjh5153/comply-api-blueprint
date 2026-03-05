#!/bin/bash

# Direct Server Test Script
# Writes all output to a file since terminal capture isn't working

OUTPUT_FILE="/tmp/server-test-results.txt"
> "$OUTPUT_FILE"

echo "Starting Spring Boot Server Test" | tee -a "$OUTPUT_FILE"
echo "Time: $(date)" | tee -a "$OUTPUT_FILE"
echo "" | tee -a "$OUTPUT_FILE"

# Kill any existing Java processes
echo "Cleaning up existing processes..." | tee -a "$OUTPUT_FILE"
pkill -f "java -jar" 2>/dev/null || true
sleep 2

# Start the server
echo "Starting Spring Boot JAR..." | tee -a "$OUTPUT_FILE"
cd /Users/mjh5153/Downloads/demo
nohup java -jar target/demo-0.0.1-SNAPSHOT.jar > /tmp/server.out 2>&1 &
SERVER_PID=$!
echo "Server PID: $SERVER_PID" | tee -a "$OUTPUT_FILE"

# Wait for startup
echo "Waiting 25 seconds for server to start..." | tee -a "$OUTPUT_FILE"
sleep 25

# Test the endpoint
echo "" | tee -a "$OUTPUT_FILE"
echo "================================" | tee -a "$OUTPUT_FILE"
echo "Testing GET /companys endpoint" | tee -a "$OUTPUT_FILE"
echo "================================" | tee -a "$OUTPUT_FILE"

echo "Attempting connection to http://localhost:8080/companys" | tee -a "$OUTPUT_FILE"

# Test without json formatting first
echo "" | tee -a "$OUTPUT_FILE"
echo "Raw Response:" | tee -a "$OUTPUT_FILE"
curl -w "\nHTTP Status: %{http_code}\n" http://localhost:8080/companys 2>&1 | tee -a "$OUTPUT_FILE"

# Try with json formatting
echo "" | tee -a "$OUTPUT_FILE"
echo "Formatted JSON:" | tee -a "$OUTPUT_FILE"
curl -s http://localhost:8080/companys 2>&1 | python3 -m json.tool 2>&1 | tee -a "$OUTPUT_FILE"

# Check server logs
echo "" | tee -a "$OUTPUT_FILE"
echo "================================" | tee -a "$OUTPUT_FILE"
echo "Server Startup Logs:" | tee -a "$OUTPUT_FILE"
echo "================================" | tee -a "$OUTPUT_FILE"
cat /tmp/server.out | tail -100 | tee -a "$OUTPUT_FILE"

# Final status
echo "" | tee -a "$OUTPUT_FILE"
echo "================================" | tee -a "$OUTPUT_FILE"
echo "Test Complete" | tee -a "$OUTPUT_FILE"
echo "Results saved to: $OUTPUT_FILE" | tee -a "$OUTPUT_FILE"
echo "Server PID: $SERVER_PID" | tee -a "$OUTPUT_FILE"
echo "Server logs at: /tmp/server.out" | tee -a "$OUTPUT_FILE"
echo "================================" | tee -a "$OUTPUT_FILE"

# Display results
echo ""
echo "TEST RESULTS:"
cat "$OUTPUT_FILE"

