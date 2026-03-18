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
 * REST Controller for COMPLY API blueprint operations.
 * Exposes endpoints for processing compliance-related company data
 * with full support for concurrent and asynchronous operations.
 *
 * Integrates with:
 * - CompanyService: Database operations
 * - AsyncHttpService: External API communication
 * - OrderFulfillmentServer: Legacy HTTP server integration
 */
@RestController
@RequestMapping("api/comply")
public class ComplyController {

    private final ComplyApiService _complyApiService;

    public ComplyController(ComplyApiService complyApiService) {
        this._complyApiService = complyApiService;
    }

    /**
     * Process a single compliance request asynchronously
     *
     * @param companyDTO Company data for compliance processing
     * @return CompletableFuture with HTTP response containing processed company
     */
    @PostMapping("/process")
    public CompletableFuture<ResponseEntity<CompanyDTO>> processCompliance(
            @RequestBody CompanyDTO companyDTO) {
        return _complyApiService.processComplianceRequest(companyDTO)
                .thenApply(processedCompany ->
                        ResponseEntity.status(HttpStatus.CREATED).body(processedCompany)
                )
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    /**
     * Process batch compliance requests in parallel
     * Handles multiple companies concurrently for bulk operations
     *
     * @param companies List of company DTOs for compliance processing
     * @return CompletableFuture with HTTP response containing processed companies
     */
    @PostMapping("/process/batch")
    public CompletableFuture<ResponseEntity<List<CompanyDTO>>> processBatchCompliance(
            @RequestBody List<CompanyDTO> companies) {
        return _complyApiService.processBatchCompliance(companies)
                .thenApply(processedCompanies ->
                        ResponseEntity.status(HttpStatus.CREATED).body(processedCompanies)
                )
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    /**
     * Send concurrent requests to external COMPLY API endpoint
     * Leverages concurrent HTTP patterns from PostConcurrentRequeststoServer
     *
     * @param apiEndpoint Target API endpoint URL
     * @param requestData Map of request parameters to send
     * @return CompletableFuture with list of API responses
     */
    @PostMapping("/external-api/concurrent")
    public CompletableFuture<ResponseEntity<List<String>>> sendConcurrentApiRequests(
            @RequestParam String apiEndpoint,
            @RequestBody Map<String, String> requestData) {
        return _complyApiService.sendConcurrentApiRequests(apiEndpoint, requestData)
                .thenApply(responses ->
                        ResponseEntity.ok().body(responses)
                )
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    /**
     * Reconcile API responses with company data
     * Processes and validates responses from external APIs
     *
     * @param apiResponses List of API response strings
     * @return CompletableFuture with reconciliation result
     */
    @PostMapping("/reconcile")
    public CompletableFuture<ResponseEntity<String>> reconcileResponses(
            @RequestBody List<String> apiResponses) {
        return _complyApiService.reconcileApiResponses(apiResponses)
                .thenApply(result ->
                        ResponseEntity.ok().body(result)
                )
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
}

