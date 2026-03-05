package com.init_spring_bean_mvn.demo.service.impl;

import com.init_spring_bean_mvn.demo.dto.CompanyDTO;
import com.init_spring_bean_mvn.demo.entity.Company;
import com.init_spring_bean_mvn.demo.mapper.CompanyMapper;
import com.init_spring_bean_mvn.demo.repository.CompanyRepository;
import com.init_spring_bean_mvn.demo.service.CompanyService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.MissingResourceException;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Implementation of CompanyService with both synchronous and asynchronous operations.
 * Async methods are executed on Spring's default async executor pool.
 */
@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository _companyRepository;
    private final CompanyMapper _companyMapper;

    public CompanyServiceImpl(CompanyRepository companyRepository, CompanyMapper companyMapper) {
        this._companyRepository = companyRepository;
        this._companyMapper = companyMapper;
    }

    /**
     * Synchronous company creation
     */
    @Override
    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        Company company = _companyMapper.toEntity(companyDTO);
        Company savedCompany = _companyRepository.save(company);
        return _companyMapper.toDTO(savedCompany);
    }

    /**
     * Synchronous company update
     */
    @Override
    public CompanyDTO updateCompany(Long id, CompanyDTO companyDTO) {
        Company company = _companyRepository.findById(id)
                .orElseThrow(() -> new MissingResourceException("Company not found", "Company", id + ""));

        company.setName(companyDTO.name());
        company.setEmail(companyDTO.email());

        Company updatedCompany = _companyRepository.save(company);
        return _companyMapper.toDTO(updatedCompany);
    }

    /**
     * Asynchronous company creation
     * Executed on Spring's async executor pool for non-blocking operation
     * Perfect for handling concurrent requests from Angular frontend
     */
    @Async
    @Override
    public CompletableFuture<CompanyDTO> createCompanyAsync(CompanyDTO companyDTO) {
        try {
            CompanyDTO result = createCompany(companyDTO);
            return CompletableFuture.completedFuture(result);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * Asynchronous batch company creation
     * Processes multiple companies concurrently using CompletableFuture
     * Ideal for bulk import operations from COMPLY API
     */
    @Override
    public CompletableFuture<List<CompanyDTO>> createCompaniesAsync(List<CompanyDTO> companies) {
        // Stream through companies and create async futures for each
        List<CompletableFuture<CompanyDTO>> futures = companies.stream()
                .map(this::createCompanyAsync)
                .toList();

        // Aggregate all futures and return combined result
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[0]))
                .thenApply(completed -> futures.stream()
                        .map(CompletableFuture::join)
                        .toList())
                .exceptionally(ex -> {
                    throw new RuntimeException("Batch company creation failed", ex);
                });
    }

    /**
     * Asynchronous company update
     * Executed on Spring's async executor pool
     */
    @Async
    @Override
    public CompletableFuture<CompanyDTO> updateCompanyAsync(Long id, CompanyDTO companyDTO) {
        try {
            CompanyDTO result = updateCompany(id, companyDTO);
            return CompletableFuture.completedFuture(result);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}
