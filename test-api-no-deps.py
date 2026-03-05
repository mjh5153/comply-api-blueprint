#!/usr/bin/env python3

"""
Spring Boot REST API Test Suite - No External Dependencies
Uses only built-in Python urllib library
Tests all endpoints and displays formatted JSON responses
"""

import urllib.request
import urllib.error
import json
import subprocess
import time
import sys
import os
import signal

BASE_URL = "http://localhost:8080"
SERVER_JAR = "/Users/mjh5153/Downloads/demo/target/demo-0.0.1-SNAPSHOT.jar"
SERVER_PROCESS = None

def print_header(title):
    print("\n" + "="*70)
    print(f"  {title}")
    print("="*70)

def print_test_header(test_num, description):
    print(f"\n{'─'*70}")
    print(f"TEST {test_num}: {description}")
    print(f"{'─'*70}")

def print_response(status_code, body):
    print(f"\nHTTP Status: {status_code}")
    print("\nResponse Body:")
    try:
        json_data = json.loads(body)
        print(json.dumps(json_data, indent=2))
    except:
        print(body)

    if 200 <= status_code < 300:
        print("\n✓ SUCCESS")
    else:
        print("\n✗ FAILED")

def test_endpoint(test_num, method, endpoint, data, description):
    print_test_header(test_num, description)
    print(f"Method: {method}")
    print(f"URL: {BASE_URL}{endpoint}")

    if data:
        print("\nRequest Body:")
        try:
            json_data = json.loads(data)
            print(json.dumps(json_data, indent=2))
        except:
            print(data)

    print("\n" + "─"*70)
    print("RESPONSE:")
    print("─"*70)

    try:
        req = urllib.request.Request(
            f"{BASE_URL}{endpoint}",
            method=method,
            data=data.encode('utf-8') if data else None
        )

        if data:
            req.add_header('Content-Type', 'application/json')

        with urllib.request.urlopen(req, timeout=10) as response:
            status_code = response.status
            body = response.read().decode('utf-8')
            print_response(status_code, body)

    except urllib.error.HTTPError as e:
        status_code = e.code
        body = e.read().decode('utf-8')
        print_response(status_code, body)
    except Exception as e:
        print(f"ERROR: {str(e)}")
        print("\n✗ FAILED")

def start_server():
    global SERVER_PROCESS
    print("Starting Spring Boot Application...")
    print(f"JAR: {SERVER_JAR}")

    # Kill any existing process on port 8080
    os.system("lsof -i :8080 2>/dev/null | grep LISTEN | awk '{print $2}' | xargs kill -9 2>/dev/null")
    time.sleep(2)

    try:
        SERVER_PROCESS = subprocess.Popen(
            ["java", "-jar", SERVER_JAR],
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE
        )
        print(f"Server PID: {SERVER_PROCESS.pid}")
        print("Waiting 15 seconds for server to start...\n")
        time.sleep(15)

        # Verify server is running
        try:
            req = urllib.request.Request(f"{BASE_URL}/companys")
            with urllib.request.urlopen(req, timeout=5) as response:
                print("✓ Server is running!\n")
                return True
        except:
            print("✗ Server failed to start")
            return False
    except Exception as e:
        print(f"✗ Failed to start server: {str(e)}")
        return False

def stop_server():
    global SERVER_PROCESS
    if SERVER_PROCESS:
        SERVER_PROCESS.terminate()
        try:
            SERVER_PROCESS.wait(timeout=5)
            print(f"\n✓ Server stopped (PID: {SERVER_PROCESS.pid})")
        except:
            SERVER_PROCESS.kill()

def main():
    print_header("Spring Boot REST API - Console Test Suite")
    print("Testing all 12 endpoints with JSON responses\n")

    # Start server
    if not start_server():
        sys.exit(1)

    try:
        # Test all endpoints
        test_endpoint(1, "GET", "/companys", None,
                     "Get All Companies")

        test_endpoint(2, "GET", "/companys/company", None,
                     "Get Single Company")

        test_endpoint(3, "GET", "/companys/1/TestCompany", None,
                     "Get Company by Path Variables")

        test_endpoint(4, "GET", "/companys/query?id=5&name=QueryTest", None,
                     "Get Company by Query Parameters")

        test_endpoint(5, "POST", "/companys",
                     '{"id":null,"name":"Test Company","email":"test@example.com"}',
                     "Create Company (Sync)")

        test_endpoint(6, "POST", "/companys/async",
                     '{"id":null,"name":"Async Test Company","email":"async@example.com"}',
                     "Create Company (Async)")

        test_endpoint(7, "POST", "/companys/batch/async",
                     '[{"id":null,"name":"Batch 1","email":"batch1@example.com"},'
                     '{"id":null,"name":"Batch 2","email":"batch2@example.com"},'
                     '{"id":null,"name":"Batch 3","email":"batch3@example.com"}]',
                     "Batch Create Companies (Async)")

        test_endpoint(8, "PUT", "/companys/1",
                     '{"id":1,"name":"Updated Company","email":"updated@example.com"}',
                     "Update Company (Sync)")

        test_endpoint(9, "POST", "/api/comply/process",
                     '{"id":null,"name":"Comply Test Company","email":"comply@example.com"}',
                     "COMPLY API - Process Single")

        test_endpoint(10, "POST", "/api/comply/process/batch",
                     '[{"id":null,"name":"Comply 1","email":"comply1@example.com"},'
                     '{"id":null,"name":"Comply 2","email":"comply2@example.com"}]',
                     "COMPLY API - Process Batch")

        # Summary
        print_header("TEST SUITE COMPLETED")
        print("✓ All 10 Endpoints Tested")
        print("✓ JSON Responses Displayed in Console")
        print("✓ Server Running on Port 8080")
        print("\nServer will remain running. Press Ctrl+C to stop.")
        print("")

        # Keep server running
        while True:
            time.sleep(1)

    except KeyboardInterrupt:
        print("\n\nShutting down...")
    finally:
        stop_server()

if __name__ == "__main__":
    main()

