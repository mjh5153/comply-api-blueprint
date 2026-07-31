package com.init_spring_bean_mvn.demo.service.impl;

import com.init_spring_bean_mvn.demo.service.AsyncHttpService;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Concurrent HTTP client backed by Java's built-in {@link HttpClient}.
 *
 * <p>Uses HTTP/2 with a fixed connect timeout. Each fan-out method builds a
 * stream of {@link HttpRequest}s, dispatches them via {@code sendAsync}, and
 * aggregates results with {@link CompletableFuture#allOf(CompletableFuture[])}.
 */
@Service
public class AsyncHttpServiceImpl implements AsyncHttpService {

    private final HttpClient _httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @Override
    public CompletableFuture<List<String>> sendConcurrentPostRequests(String baseUrl,
                                                                      Map<String, String> requestData) {
        List<CompletableFuture<String>> futures = requestData.entrySet().stream()
                .map(entry -> HttpRequest.newBuilder(URI.create(baseUrl))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(
                                "{\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"}"))
                        .build())
                .map(request -> _httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenApply(HttpResponse::body))
                .toList();

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .toList());
    }

    @Override
    public CompletableFuture<String> sendAsyncPostRequest(String url, String requestBody) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        return _httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }

    @Override
    public CompletableFuture<String> sendAsyncGetRequest(String url) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().build();
        return _httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }
}

