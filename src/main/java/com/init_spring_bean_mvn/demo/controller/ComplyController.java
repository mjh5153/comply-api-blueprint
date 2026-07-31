package com.init_spring_bean_mvn.demo.controller;

import com.init_spring_bean_mvn.demo.dto.CompanyDTO;
import com.init_spring_bean_mvn.demo.service.ComplyApiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * COMPLY API blueprint endpoints.
 *
 * <p>Wraps {@link ComplyApiService} to offer non-blocking compliance-processing
 * endpoints. The batch and external-API routes demonstrate concurrent
 * {@link CompletableFuture} pipelines aggregated with {@code allOf}.
 *
 * <p>Base path: {@code /api/comply}
 */
@RestController
@RequestMapping("/api/comply")
public class ComplyController {

    private final ComplyApiService _complyApiService;

    public ComplyController(ComplyApiService complyApiService) {
        this._complyApiService = complyApiService;
    }

    /** Process one compliance request asynchronously. */
    @PostMapping("/process")
    public CompletableFuture<ResponseEntity<CompanyDTO>> processCompliance(
            @RequestBody CompanyDTO companyDTO) {
        return _complyApiService.processComplianceRequest(companyDTO)
                .thenApply(saved -> ResponseEntity.status(HttpStatus.CREATED).body(saved))
                .exceptionally(ex ->
                        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    /** Process a batch of compliance requests in parallel. */
    @PostMapping("/process/batch")
    public CompletableFuture<ResponseEntity<List<CompanyDTO>>> processBatchCompliance(
            @RequestBody List<CompanyDTO> companies) {
        return _complyApiService.processBatchCompliance(companies)
                .thenApply(saved -> ResponseEntity.status(HttpStatus.CREATED).body(saved))
                .exceptionally(ex ->
                        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    /** Fan out concurrent HTTP POSTs to an external endpoint. */
    @PostMapping("/external-api/concurrent")
    public CompletableFuture<ResponseEntity<List<String>>> sendConcurrentApiRequests(
            @RequestParam String apiEndpoint,
            @RequestBody Map<String, String> requestData) {
        return _complyApiService.sendConcurrentApiRequests(apiEndpoint, requestData)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex ->
                        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    /** Reconcile a batch of external API responses. */
    @PostMapping("/reconcile")
    public CompletableFuture<ResponseEntity<String>> reconcileResponses(
            @RequestBody List<String> apiResponses) {
        return _complyApiService.reconcileApiResponses(apiResponses)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex ->
                        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
}
