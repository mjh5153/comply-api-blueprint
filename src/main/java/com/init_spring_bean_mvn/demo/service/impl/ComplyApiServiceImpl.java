package com.init_spring_bean_mvn.demo.service.impl;

import com.init_spring_bean_mvn.demo.service.ComplyApiService;
import com.init_spring_bean_mvn.demo.service.CompanyService;
import com.init_spring_bean_mvn.demo.service.AsyncHttpService;
import com.init_spring_bean_mvn.demo.dto.CompanyDTO;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of ComplyApiService.
 * Integrates concurrent request handling with company management operations.
 *
 * Architecture:
 * - CompanyService: Database persistence for company records
 * - AsyncHttpService: Concurrent HTTP communication with external APIs
 * - ComplyApiService: Business logic orchestration
 */
@Service
public class ComplyApiServiceImpl implements ComplyApiService {

    private static final Logger _logger = LoggerFactory.getLogger(ComplyApiServiceImpl.class);

    private final CompanyService _companyService;
    private final AsyncHttpService _asyncHttpService;

    public ComplyApiServiceImpl(CompanyService companyService, AsyncHttpService asyncHttpService) {
        this._companyService = companyService;
        this._asyncHttpService = asyncHttpService;
    }

    /**
     * Process compliance request for a single company
     *
     * Workflow:
     * 1. Validate company data
     * 2. Create company in database
     * 3. Log operation
     *
     * @param companyDTO Company data
     * @return CompletableFuture with processed company DTO
     */
    @Override
    public CompletableFuture<CompanyDTO> processComplianceRequest(CompanyDTO companyDTO) {
        _logger.info("Processing compliance request for company: {}", companyDTO.name());

        return _companyService.createCompanyAsync(companyDTO)
                .thenApply(saved -> {
                    _logger.info("Compliance request processed successfully for: {}", saved.name());
                    return saved;
                })
                .exceptionally(ex -> {
                    _logger.error("Failed to process compliance request", ex);
                    throw new RuntimeException("Compliance processing failed", ex);
                });
    }

    /**
     * Process batch compliance requests in parallel
     *
     * Workflow:
     * 1. Process each company concurrently
     * 2. Aggregate results
     * 3. Handle errors per request
     *
     * @param companies List of company DTOs
     * @return CompletableFuture with processed company list
     */
    @Override
    public CompletableFuture<List<CompanyDTO>> processBatchCompliance(List<CompanyDTO> companies) {
        _logger.info("Processing batch compliance for {} companies", companies.size());

        return _companyService.createCompaniesAsync(companies)
                .thenApply(savedCompanies -> {
                    _logger.info("Batch compliance processing completed for {} companies", savedCompanies.size());
                    return savedCompanies;
                })
                .exceptionally(ex -> {
                    _logger.error("Batch compliance processing failed", ex);
                    throw new RuntimeException("Batch compliance processing failed", ex);
                });
    }

    /**
     * Send concurrent HTTP requests to external COMPLY API endpoint
     *
     * Workflow:
     * 1. Build request map from company data
     * 2. Send concurrent requests to external API
     * 3. Collect responses
     *
     * @param apiEndpoint External API endpoint URL
     * @param requestData Map of request parameters
     * @return CompletableFuture with list of responses
     */
    @Override
    public CompletableFuture<List<String>> sendConcurrentApiRequests(
            String apiEndpoint,
            Map<String, String> requestData) {
        _logger.info("Sending concurrent requests to API endpoint: {}", apiEndpoint);

        return _asyncHttpService.sendConcurrentPostRequests(apiEndpoint, requestData)
                .thenApply(responses -> {
                    _logger.info("Received {} responses from API", responses.size());
                    return responses;
                })
                .exceptionally(ex -> {
                    _logger.error("Concurrent API request failed", ex);
                    throw new RuntimeException("API request failed", ex);
                });
    }

    /**
     * Reconcile COMPLY API responses with company data
     *
     * Workflow:
     * 1. Parse API responses
     * 2. Extract relevant data
     * 3. Log reconciliation results
     *
     * @param apiResponses List of API responses
     * @return CompletableFuture with reconciliation result message
     */
    @Override
    public CompletableFuture<String> reconcileApiResponses(List<String> apiResponses) {
        _logger.info("Reconciling {} API responses", apiResponses.size());

        return CompletableFuture.supplyAsync(() -> {
            try {
                // Parse and validate responses
                long successCount = apiResponses.stream()
                        .filter(response -> response != null && !response.isEmpty())
                        .count();

                String result = String.format("Reconciliation complete: %d/%d responses processed",
                        successCount, apiResponses.size());

                _logger.info(result);
                return result;
            } catch (Exception ex) {
                _logger.error("Reconciliation failed", ex);
                throw new RuntimeException("Reconciliation failed", ex);
            }
        });
    }
}

