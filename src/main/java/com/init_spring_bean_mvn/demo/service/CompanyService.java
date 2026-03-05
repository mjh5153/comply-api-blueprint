package com.init_spring_bean_mvn.demo.service;

import com.init_spring_bean_mvn.demo.dto.CompanyDTO;
import java.util.concurrent.CompletableFuture;
import java.util.List;

/**
 * Service interface for Company management with both synchronous and asynchronous operations.
 * Supports concurrent request handling for COMPLY API blueprint integration.
 */
public interface CompanyService {

    /**
     * Synchronous company creation
     * @param companyDTO Company data transfer object
     * @return Created company DTO
     */
    CompanyDTO createCompany(CompanyDTO companyDTO);

    /**
     * Synchronous company update
     * @param id Company ID
     * @param companyDTO Updated company data
     * @return Updated company DTO
     */
    CompanyDTO updateCompany(Long id, CompanyDTO companyDTO);

    /**
     * Asynchronous company creation
     * Enables concurrent processing from Angular frontend requests
     *
     * @param companyDTO Company data transfer object
     * @return CompletableFuture with created company DTO
     */
    CompletableFuture<CompanyDTO> createCompanyAsync(CompanyDTO companyDTO);

    /**
     * Asynchronous batch company creation
     * Processes multiple company creation requests in parallel
     *
     * @param companies List of company DTOs
     * @return CompletableFuture with list of created companies
     */
    CompletableFuture<List<CompanyDTO>> createCompaniesAsync(List<CompanyDTO> companies);

    /**
     * Asynchronous company update
     *
     * @param id Company ID
     * @param companyDTO Updated company data
     * @return CompletableFuture with updated company DTO
     */
    CompletableFuture<CompanyDTO> updateCompanyAsync(Long id, CompanyDTO companyDTO);
}
