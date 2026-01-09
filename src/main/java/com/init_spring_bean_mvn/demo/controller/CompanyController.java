package com.init_spring_bean_mvn.demo.controller;

import com.init_spring_bean_mvn.demo.bean.Company;
import com.init_spring_bean_mvn.demo.dto.CompanyDTO;
import com.init_spring_bean_mvn.demo.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
// configure base url for all apis - define at class level
@RestController
@RequestMapping("companys")
public class CompanyController {

    //Spring team uses interfaces - mock in unit testing
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    // build create company rest api

    @PostMapping
    public ResponseEntity<CompanyDTO> createCompany(@RequestBody CompanyDTO companyDTO) {
        CompanyDTO savedCompany = companyService.createCompany(companyDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCompany);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyDTO> updateCompany(@PathVariable Long id, @RequestBody CompanyDTO companyDTO) {
        CompanyDTO updatedCompany = companyService.updateCompany(id, companyDTO);
        return ResponseEntity.ok().body(updatedCompany);
    }

    //create springboot class that returns java been as json
    // http://localhost:8080/student
    @GetMapping("company")
    public ResponseEntity<Company> getCompany() {
        Company company = new Company(
                "CompanyTest",
                1
        );
        return ResponseEntity.ok().header("custom-header", "Joel").body(company);
    }

    //make rest api by spring annotation
    // http://localhost:8080/companys
    @GetMapping
    public List<Company> getCompanys() {
        List<Company> companys = new ArrayList<>();
        companys.add(new Company("InvestCross", 1));
        companys.add(new Company("Karen", 2));

        companys.add(new Company("Audiology", 3));
        companys.add(new Company("Murphy", 4));
        return companys;
    }

    // Spring boot rest api with path variable
    @GetMapping("{id}/{name}") // bind path param id to id
    public Company companyPathVariable(@PathVariable int id, @PathVariable String name) {
        return new Company(name, id);
    }

    //Spring boot REST API with Request Param
    // http://localhost:8080/companys/query?id=1&name=Karen

    @GetMapping("query")
    public Company companyRequestVariable(@RequestParam int id, @RequestParam String name) {
        return new Company(name, id);
    }

    // Spring boot REST API that handles HTTP post request
    // @PostMapping and @RequestBody annotation

    @PostMapping("create")
    @ResponseStatus(HttpStatus.CREATED)
    public Company createCompany(@RequestBody Company company) { // internally uses Spring provided HttpMessageConverter to convert JSON to java object
        System.out.println(company.getId());
        System.out.println(company.getName());
        return company;
    }

    // HTTP PUT request - update existing resource

    @PutMapping("{id}/update")

    public Company updateStudent(@RequestBody Company company, @PathVariable int id) {
        System.out.println(company.getName());
        return company;

    }
}
