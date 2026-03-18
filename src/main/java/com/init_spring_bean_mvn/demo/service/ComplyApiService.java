package com.init_spring_bean_mvn.demo.service;

import com.init_spring_bean_mvn.demo.dto.CompanyDTO;
import java.util.concurrent.CompletableFuture;
import java.util.List;
import java.util.Map;

/**
 * Service for COMPLY API blueprint integration.
 * Bridges concurrent request handling with company management operations.
 *
 * Provides high-level operations for processing compliance-related company data
 * with full support for concurrent and parallel request processing.
 */
public interface ComplyApiService {

    /**
     * Process compliance request for a company asynchronously
     *
     * @param companyDTO Company data
     * @return CompletableFuture with processed company DTO
     */
    CompletableFuture<CompanyDTO> processComplianceRequest(CompanyDTO companyDTO);

    /**
     * Process batch compliance requests in parallel
     * Leverages concurrent request patterns for bulk company operations
     *
     * @param companies List of company DTOs
     * @return CompletableFuture with processed company list
     */
    CompletableFuture<List<CompanyDTO>> processBatchCompliance(List<CompanyDTO> companies);

    /**
     * Send concurrent HTTP requests to external COMPLY API endpoint
     * Integrates with AsyncHttpService for external API communication
     *
     * @param apiEndpoint External API endpoint URL
     * @param requestData Map of request parameters
     * @return CompletableFuture with list of responses
     */
    CompletableFuture<List<String>> sendConcurrentApiRequests(
            String apiEndpoint,
            Map<String, String> requestData
    );

    /**
     * Reconcile COMPLY API responses with company data
     * Processes responses and updates company records
     *
     * @param apiResponses List of API responses
     * @return CompletableFuture with reconciliation result
     */
    CompletableFuture<String> reconcileApiResponses(List<String> apiResponses);
}

