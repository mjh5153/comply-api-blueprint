package io.github.mjh5153.complyapi.controller;

import io.github.mjh5153.complyapi.dto.CompanyDTO;
import io.github.mjh5153.complyapi.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * REST endpoints for {@code Company} management.
 *
 * <p>Every mutating operation is exposed in both a synchronous form (blocking,
 * simple) and an asynchronous form returning {@link CompletableFuture}
 * (non-blocking, higher throughput under concurrent load). All endpoints are
 * backed by JPA persistence via {@link CompanyService}.
 *
 * <p>Base path: {@code /companies}
 */
@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService _companyService;

    public CompanyController(CompanyService companyService) {
        this._companyService = companyService;
    }

    // --- Reads -----------------------------------------------------------

    @GetMapping
    public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
        return ResponseEntity.ok(_companyService.getAllCompanies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> getCompany(@PathVariable Long id) {
        return ResponseEntity.ok(_companyService.getCompany(id));
    }

    // --- Sync writes -----------------------------------------------------

    @PostMapping
    public ResponseEntity<CompanyDTO> createCompany(@RequestBody CompanyDTO companyDTO) {
        CompanyDTO saved = _companyService.createCompany(companyDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyDTO> updateCompany(@PathVariable Long id,
                                                    @RequestBody CompanyDTO companyDTO) {
        return ResponseEntity.ok(_companyService.updateCompany(id, companyDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        _companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }

    // --- Async writes ----------------------------------------------------

    /**
     * Non-blocking single-company create. The request thread is released as
     * soon as the future is scheduled onto the async executor.
     */
    @PostMapping("/async")
    public CompletableFuture<ResponseEntity<CompanyDTO>> createCompanyAsync(
            @RequestBody CompanyDTO companyDTO) {
        return _companyService.createCompanyAsync(companyDTO)
                .thenApply(saved -> ResponseEntity.status(HttpStatus.CREATED).body(saved))
                .exceptionally(ex ->
                        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    /**
     * Non-blocking batch create. Companies are persisted in parallel and
     * results are aggregated with {@link CompletableFuture#allOf}.
     */
    @PostMapping("/batch/async")
    public CompletableFuture<ResponseEntity<List<CompanyDTO>>> createCompaniesAsync(
            @RequestBody List<CompanyDTO> companies) {
        return _companyService.createCompaniesAsync(companies)
                .thenApply(saved -> ResponseEntity.status(HttpStatus.CREATED).body(saved))
                .exceptionally(ex ->
                        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    /** Non-blocking update. */
    @PutMapping("/{id}/async")
    public CompletableFuture<ResponseEntity<CompanyDTO>> updateCompanyAsync(
            @PathVariable Long id,
            @RequestBody CompanyDTO companyDTO) {
        return _companyService.updateCompanyAsync(id, companyDTO)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex ->
                        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
}
