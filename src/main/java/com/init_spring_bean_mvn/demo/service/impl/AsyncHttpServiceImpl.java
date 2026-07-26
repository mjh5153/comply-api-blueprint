package com.init_spring_bean_mvn.demo.service.impl;

import com.init_spring_bean_mvn.demo.service.AsyncHttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class AsyncHttpServiceImpl implements AsyncHttpService {

    private static final Logger _logger = LoggerFactory.getLogger(AsyncHttpServiceImpl.class);
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(5);

    private final HttpClient _client = HttpClient.newBuilder()
            .connectTimeout(REQUEST_TIMEOUT)
            .build();

    @Override
    public CompletableFuture<List<String>> sendConcurrentPostRequests(
            String baseUrl,
            Map<String, String> requestData) {
        List<CompletableFuture<String>> futures = requestData.entrySet().stream()
                .map(entry -> sendAsyncPostRequest(baseUrl, "%s=%s".formatted(entry.getKey(), entry.getValue())))
                .toList();

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[0]))
                .thenApply(unused -> futures.stream()
                        .map(CompletableFuture::join)
                        .toList());
    }

    @Override
    public CompletableFuture<String> sendAsyncPostRequest(String url, String requestBody) {
        HttpRequest request;
        try {
            request = HttpRequest.newBuilder(URI.create(url))
                    .timeout(REQUEST_TIMEOUT)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
        } catch (IllegalArgumentException ex) {
            return CompletableFuture.completedFuture(errorMessage("POST", url, ex));
        }

        return _client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .exceptionally(ex -> errorMessage("POST", url, ex));
    }

    @Override
    public CompletableFuture<String> sendAsyncGetRequest(String url) {
        HttpRequest request;
        try {
            request = HttpRequest.newBuilder(URI.create(url))
                    .timeout(REQUEST_TIMEOUT)
                    .GET()
                    .build();
        } catch (IllegalArgumentException ex) {
            return CompletableFuture.completedFuture(errorMessage("GET", url, ex));
        }

        return _client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .exceptionally(ex -> errorMessage("GET", url, ex));
    }

    private String errorMessage(String method, String url, Throwable ex) {
        String message = "%s %s failed: %s".formatted(method, url, ex.getMessage());
        _logger.warn(message);
        return message;
    }
}
