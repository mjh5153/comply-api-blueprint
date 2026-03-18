package com.init_spring_bean_mvn.demo.http;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

/**
 * HTTP Client utility for testing backend endpoints
 * Demonstrates how to make HTTP requests to the async REST API
 * Can be run as a standalone application or integrated with tests
 */
public class HttpClientTest {

    private static final String BASE_URL = "http://localhost:8080";
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    public static void main(String[] args) {
        System.out.println("=== HTTP Client Test Suite ===\n");

        try {
            // Test synchronous company creation
            testSyncCompanyCreation();

            // Test asynchronous company creation
            testAsyncCompanyCreation();

            // Test batch async company creation
            testBatchAsyncCompanyCreation();

            // Test get all companies
            testGetAllCompanies();

            // Test COMPLY API single process
            testComplyProcessSingle();

            // Test COMPLY API batch process
            testComplyProcessBatch();

            // Test concurrent API requests
            testConcurrentApiRequests();

            System.out.println("\n=== All Tests Completed ===");
        } catch (Exception e) {
            System.err.println("Error during testing: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Test synchronous company creation
     */
    static void testSyncCompanyCreation() throws Exception {
        System.out.println("Testing: Sync Company Creation (POST /companys)");

        String json = """
                {
                    "id": null,
                    "name": "Sync Test Company",
                    "email": "sync@example.com"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + "/companys"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status: " + response.statusCode());
        System.out.println("Response: " + response.body());
        System.out.println("✓ Test passed\n");
    }

    /**
     * Test asynchronous company creation
     */
    static void testAsyncCompanyCreation() throws Exception {
        System.out.println("Testing: Async Company Creation (POST /companys/async)");

        String json = """
                {
                    "id": null,
                    "name": "Async Test Company",
                    "email": "async@example.com"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + "/companys/async"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status: " + response.statusCode());
        System.out.println("Response: " + response.body());
        System.out.println("✓ Test passed\n");
    }

    /**
     * Test batch async company creation
     */
    static void testBatchAsyncCompanyCreation() throws Exception {
        System.out.println("Testing: Batch Async Company Creation (POST /companys/batch/async)");

        String json = """
                [
                    {
                        "id": null,
                        "name": "Batch Company 1",
                        "email": "batch1@example.com"
                    },
                    {
                        "id": null,
                        "name": "Batch Company 2",
                        "email": "batch2@example.com"
                    },
                    {
                        "id": null,
                        "name": "Batch Company 3",
                        "email": "batch3@example.com"
                    }
                ]
                """;

        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + "/companys/batch/async"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status: " + response.statusCode());
        System.out.println("Response: " + response.body());
        System.out.println("✓ Test passed\n");
    }

    /**
     * Test get all companies
     */
    static void testGetAllCompanies() throws Exception {
        System.out.println("Testing: Get All Companies (GET /companys)");

        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + "/companys"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status: " + response.statusCode());
        System.out.println("Response: " + response.body());
        System.out.println("✓ Test passed\n");
    }

    /**
     * Test COMPLY API single process
     */
    static void testComplyProcessSingle() throws Exception {
        System.out.println("Testing: COMPLY Process Single (POST /api/comply/process)");

        String json = """
                {
                    "id": null,
                    "name": "COMPLY Test Company",
                    "email": "comply@example.com"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + "/api/comply/process"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status: " + response.statusCode());
        System.out.println("Response: " + response.body());
        System.out.println("✓ Test passed\n");
    }

    /**
     * Test COMPLY API batch process
     */
    static void testComplyProcessBatch() throws Exception {
        System.out.println("Testing: COMPLY Process Batch (POST /api/comply/process/batch)");

        String json = """
                [
                    {
                        "id": null,
                        "name": "COMPLY Batch 1",
                        "email": "comply-batch1@example.com"
                    },
                    {
                        "id": null,
                        "name": "COMPLY Batch 2",
                        "email": "comply-batch2@example.com"
                    }
                ]
                """;

        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + "/api/comply/process/batch"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status: " + response.statusCode());
        System.out.println("Response: " + response.body());
        System.out.println("✓ Test passed\n");
    }

    /**
     * Test concurrent API requests to OrderFulfillmentServer
     */
    static void testConcurrentApiRequests() throws Exception {
        System.out.println("Testing: Concurrent API Requests (POST /api/comply/external-api/concurrent)");

        String requestData = """
                {
                    "product": "apples",
                    "amount": "500",
                    "company": "InvestCross"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder(
                URI.create(BASE_URL + "/api/comply/external-api/concurrent?apiEndpoint=http://localhost:8080"))
                .POST(HttpRequest.BodyPublishers.ofString(requestData))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status: " + response.statusCode());
        System.out.println("Response: " + response.body());
        System.out.println("✓ Test passed\n");
    }

    /**
     * Helper method to send async requests and handle responses
     */
    static CompletableFuture<String> sendAsyncRequest(String endpoint, String json) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + endpoint))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }
}

