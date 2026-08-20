package io.github.mjh5153.complyapi.service;

import io.github.mjh5153.complyapi.dto.CompanyDTO;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Business operations for {@code Company} records.
 *
 * <p>Every write method is offered in both synchronous and asynchronous form so
 * callers can choose between blocking simplicity and non-blocking throughput.
 * Async methods run on Spring's {@code taskExecutor} thread pool
 * (see {@code AsyncConfig}).
 */
public interface CompanyService {

    // --- Reads -----------------------------------------------------------

    List<CompanyDTO> getAllCompanies();

    CompanyDTO getCompany(Long id);

    // --- Sync writes -----------------------------------------------------

    CompanyDTO createCompany(CompanyDTO companyDTO);

    CompanyDTO updateCompany(Long id, CompanyDTO companyDTO);

    void deleteCompany(Long id);

    // --- Async writes ----------------------------------------------------

    CompletableFuture<CompanyDTO> createCompanyAsync(CompanyDTO companyDTO);

    CompletableFuture<List<CompanyDTO>> createCompaniesAsync(List<CompanyDTO> companies);

    CompletableFuture<CompanyDTO> updateCompanyAsync(Long id, CompanyDTO companyDTO);
}
