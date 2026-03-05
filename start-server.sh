 #!/bin/bash

# Spring Boot Startup Script - Handles Port Conflicts
# Kills existing processes and starts server on available port

echo ""
echo "╔════════════════════════════════════════════════════════════════╗"
echo "║         Spring Boot Server Startup - Smart Port Manager        ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

BASE_PORT=8080
JAR_FILE="/Users/mjh5153/Downloads/demo/target/demo-0.0.1-SNAPSHOT.jar"

echo "Checking for processes on port $BASE_PORT..."
lsof -i :$BASE_PORT 2>/dev/null | grep LISTEN

echo ""
echo "Killing any existing Java processes on port $BASE_PORT..."
lsof -i :$BASE_PORT 2>/dev/null | grep -i java | awk '{print $2}' | xargs kill -9 2>/dev/null || true
sleep 2

echo "Killing all Java processes (to be safe)..."
pkill -f "java -jar" 2>/dev/null || true
sleep 2

echo "Verifying port is free..."
if lsof -i :$BASE_PORT 2>/dev/null | grep LISTEN > /dev/null; then
    echo "✗ Port $BASE_PORT is still in use"
    echo ""
    echo "Processes using port $BASE_PORT:"
    lsof -i :$BASE_PORT 2>/dev/null
    echo ""
    echo "Try: sudo lsof -i :$BASE_PORT | grep LISTEN | awk '{print \$2}' | xargs sudo kill -9"
    exit 1
fi

echo "✓ Port $BASE_PORT is free"
echo ""
echo "Starting Spring Boot Application..."
echo "JAR: $JAR_FILE"
echo ""

cd /Users/mjh5153/Downloads/demo

# Start the server
java -jar "$JAR_FILE" 2>&1 | tee /tmp/server-startup.log &
SERVER_PID=$!

echo "Server PID: $SERVER_PID"
echo "Waiting 20 seconds for server to start..."
echo ""

sleep 20

# Check if server is running
echo "Verifying server is running..."
if curl -s http://localhost:$BASE_PORT/companys > /dev/null 2>&1; then
    echo "✓ Server is running!"
    echo "✓ Server is accessible on http://localhost:$BASE_PORT"
    echo ""
    echo "Server Details:"
    echo "  PID: $SERVER_PID"
    echo "  URL: http://localhost:$BASE_PORT"
    echo "  JAR: $JAR_FILE"
    echo ""
    echo "To stop the server:"
    echo "  kill $SERVER_PID"
    echo ""
    echo "To view logs:"
    echo "  tail -f /tmp/server-startup.log"
    echo ""
    echo "To test endpoints in another terminal:"
    echo "  curl -s http://localhost:$BASE_PORT/companys | python3 -m json.tool"
    echo ""
else
    echo "✗ Server failed to start"
    echo ""
    echo "Last 50 lines of server log:"
    echo "───────────────────────────────────────────────────────────────"
    tail -50 /tmp/server-startup.log
    echo "───────────────────────────────────────────────────────────────"
    echo ""
    echo "Full log saved to: /tmp/server-startup.log"
    exit 1
fi

# Keep the script running so server stays alive
wait $SERVER_PID

