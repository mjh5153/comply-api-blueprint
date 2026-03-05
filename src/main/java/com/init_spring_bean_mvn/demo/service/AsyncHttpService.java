package com.init_spring_bean_mvn.demo.service;

import java.util.concurrent.CompletableFuture;
import java.util.List;
import java.util.Map;

/**
 * Service for handling asynchronous HTTP operations with concurrency and parallelism.
 * Integrates concurrent request patterns from PostConcurrentRequeststoServer
 * into Spring Boot service layer.
 */
public interface AsyncHttpService {

    /**
     * Send multiple asynchronous POST requests in parallel
     * @param baseUrl Base URL for requests
     * @param requestData Map of data to send
     * @return CompletableFuture containing list of responses
     */
    CompletableFuture<List<String>> sendConcurrentPostRequests(
            String baseUrl,
            Map<String, String> requestData
    );

    /**
     * Send a single asynchronous POST request
     * @param url Target URL
     * @param requestBody Request body
     * @return CompletableFuture containing response
     */
    CompletableFuture<String> sendAsyncPostRequest(
            String url,
            String requestBody
    );

    /**
     * Send asynchronous GET request
     * @param url Target URL
     * @return CompletableFuture containing response
     */
    CompletableFuture<String> sendAsyncGetRequest(String url);
}

