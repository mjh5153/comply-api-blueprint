package io.github.mjh5153.complyapi.service.impl;

import io.github.mjh5153.complyapi.dto.CompanyDTO;
import io.github.mjh5153.complyapi.entity.Company;
import io.github.mjh5153.complyapi.exception.ResourceNotFoundException;
import io.github.mjh5153.complyapi.mapper.CompanyMapper;
import io.github.mjh5153.complyapi.repository.CompanyRepository;
import io.github.mjh5153.complyapi.service.CompanyService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Default {@link CompanyService} implementation.
 *
 * <p>Async methods are annotated with {@link Async} so they execute on the
 * {@code taskExecutor} pool defined in {@code AsyncConfig}. Batch operations
 * fan out per-company async calls and aggregate them with
 * {@link CompletableFuture#allOf(CompletableFuture[])}.
 */
@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository _companyRepository;
    private final CompanyMapper _companyMapper;

    public CompanyServiceImpl(CompanyRepository companyRepository, CompanyMapper companyMapper) {
        this._companyRepository = companyRepository;
        this._companyMapper = companyMapper;
    }

    // --- Reads -----------------------------------------------------------

    @Override
    public List<CompanyDTO> getAllCompanies() {
        return _companyRepository.findAll().stream()
                .map(_companyMapper::toDTO)
                .toList();
    }

    @Override
    public CompanyDTO getCompany(Long id) {
        Company company = _companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company " + id + " not found"));
        return _companyMapper.toDTO(company);
    }

    // --- Sync writes -----------------------------------------------------

    @Override
    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        Company saved = _companyRepository.save(_companyMapper.toEntity(companyDTO));
        return _companyMapper.toDTO(saved);
    }

    @Override
    public CompanyDTO updateCompany(Long id, CompanyDTO companyDTO) {
        Company company = _companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company " + id + " not found"));
        company.setName(companyDTO.name());
        company.setEmail(companyDTO.email());
        return _companyMapper.toDTO(_companyRepository.save(company));
    }

    @Override
    public void deleteCompany(Long id) {
        if (!_companyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Company " + id + " not found");
        }
        _companyRepository.deleteById(id);
    }

    // --- Async writes ----------------------------------------------------

    @Async
    @Override
    public CompletableFuture<CompanyDTO> createCompanyAsync(CompanyDTO companyDTO) {
        try {
            return CompletableFuture.completedFuture(createCompany(companyDTO));
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    @Override
    public CompletableFuture<List<CompanyDTO>> createCompaniesAsync(List<CompanyDTO> companies) {
        List<CompletableFuture<CompanyDTO>> futures = companies.stream()
                .map(this::createCompanyAsync)
                .toList();

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .toList());
    }

    @Async
    @Override
    public CompletableFuture<CompanyDTO> updateCompanyAsync(Long id, CompanyDTO companyDTO) {
        try {
            return CompletableFuture.completedFuture(updateCompany(id, companyDTO));
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}
