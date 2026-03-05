package com.init_spring_bean_mvn.demo.controller;

import com.init_spring_bean_mvn.demo.bean.Company;
import com.init_spring_bean_mvn.demo.dto.CompanyDTO;
import com.init_spring_bean_mvn.demo.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * REST Controller for Company management with COMPLY API integration
 * Supports both synchronous and asynchronous request handling
 *
 * Endpoints receive requests with concurrency and parallelism from Angular frontend
 * All async operations are non-blocking and thread-safe
 */
@RestController
@RequestMapping("companys")
public class CompanyController {

    private final CompanyService _companyService;

    public CompanyController(CompanyService companyService) {
        this._companyService = companyService;
    }

    /**
     * Synchronous company creation (Original)
     * @param companyDTO Company data to create
     * @return ResponseEntity with created company
     */
    @PostMapping
    public ResponseEntity<CompanyDTO> createCompany(@RequestBody CompanyDTO companyDTO) {
        CompanyDTO savedCompany = _companyService.createCompany(companyDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCompany);
    }

    /**
     * Asynchronous company creation for concurrent requests
     * Processes request non-blocking on async executor pool
     * Ideal for high-concurrency scenarios from Angular frontend
     *
     * @param companyDTO Company data to create
     * @return CompletableFuture<ResponseEntity<CompanyDTO>>
     */
    @PostMapping("/async")
    public CompletableFuture<ResponseEntity<CompanyDTO>> createCompanyAsync(
            @RequestBody CompanyDTO companyDTO) {
        return _companyService.createCompanyAsync(companyDTO)
                .thenApply(savedCompany ->
                        ResponseEntity.status(HttpStatus.CREATED).body(savedCompany)
                )
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    /**
     * Batch asynchronous company creation
     * Handles multiple concurrent company creation requests in parallel
     * Leverages CompletableFuture for optimal concurrency
     * Perfect for COMPLY API bulk operations
     *
     * @param companies List of company DTOs to create
     * @return CompletableFuture<ResponseEntity<List<CompanyDTO>>>
     */
    @PostMapping("/batch/async")
    public CompletableFuture<ResponseEntity<List<CompanyDTO>>> createCompaniesAsync(
            @RequestBody List<CompanyDTO> companies) {
        return _companyService.createCompaniesAsync(companies)
                .thenApply(savedCompanies ->
                        ResponseEntity.status(HttpStatus.CREATED).body(savedCompanies)
                )
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    /**
     * Synchronous company update (Original)
     * @param id Company ID to update
     * @param companyDTO Updated company data
     * @return ResponseEntity with updated company
     */
    @PutMapping("/{id}")
    public ResponseEntity<CompanyDTO> updateCompany(
            @PathVariable Long id,
            @RequestBody CompanyDTO companyDTO) {
        CompanyDTO updatedCompany = _companyService.updateCompany(id, companyDTO);
        return ResponseEntity.ok().body(updatedCompany);
    }

    /**
     * Asynchronous company update for concurrent requests
     * Non-blocking update operation
     *
     * @param id Company ID to update
     * @param companyDTO Updated company data
     * @return CompletableFuture<ResponseEntity<CompanyDTO>>
     */
    @PutMapping("/{id}/async")
    public CompletableFuture<ResponseEntity<CompanyDTO>> updateCompanyAsync(
            @PathVariable Long id,
            @RequestBody CompanyDTO companyDTO) {
        return _companyService.updateCompanyAsync(id, companyDTO)
                .thenApply(updatedCompany -> ResponseEntity.ok().body(updatedCompany))
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    /**
     * Get single company (Original)
     * @return ResponseEntity with company
     */
    @GetMapping("company")
    public ResponseEntity<Company> getCompany() {
        Company company = new Company(
                "CompanyTest",
                1
        );
        return ResponseEntity.ok().header("custom-header", "Joel").body(company);
    }

    /**
     * Get all companies (Original)
     * @return List of companies
     */
    @GetMapping
    public List<Company> getCompanys() {
        List<Company> companys = new ArrayList<>();
        companys.add(new Company("InvestCross", 1));
        companys.add(new Company("Karen", 2));
        companys.add(new Company("Audiology", 3));
        companys.add(new Company("Murphy", 4));
        return companys;
    }

    /**
     * Get company by path variables (Original)
     * @param id Company ID
     * @param name Company name
     * @return Company object
     */
    @GetMapping("{id}/{name}")
    public Company companyPathVariable(
            @PathVariable int id,
            @PathVariable String name) {
        return new Company(name, id);
    }

    /**
     * Get company by query parameters (Original)
     * @param id Company ID
     * @param name Company name
     * @return Company object
     */
    @GetMapping("query")
    public Company companyRequestVariable(
            @RequestParam int id,
            @RequestParam String name) {
        return new Company(name, id);
    }

    /**
     * Create company from form body (Original)
     * @param company Company object
     * @return Created company
     */
    @PostMapping("create")
    @ResponseStatus(HttpStatus.CREATED)
    public Company createCompanyForm(@RequestBody Company company) {
        System.out.println(company.getId());
        System.out.println(company.getName());
        return company;
    }

    /**
     * Update company from path variable (Original)
     * @param company Updated company data
     * @param id Company ID
     * @return Updated company
     */
    @PutMapping("{id}/update")
    public Company updateStudent(
            @RequestBody Company company,
            @PathVariable int id) {
        System.out.println(company.getName());
        return company;

    }
}
