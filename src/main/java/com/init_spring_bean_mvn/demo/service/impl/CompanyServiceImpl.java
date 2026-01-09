package com.init_spring_bean_mvn.demo.service.impl;

import com.init_spring_bean_mvn.demo.dto.CompanyDTO;
import com.init_spring_bean_mvn.demo.entity.Company;
import com.init_spring_bean_mvn.demo.exception.ResourceNotFoundException;
import com.init_spring_bean_mvn.demo.mapper.CompanyMapper;
import com.init_spring_bean_mvn.demo.repository.CompanyRepository;
import com.init_spring_bean_mvn.demo.service.CompanyService;
import org.springframework.stereotype.Service;

import java.util.MissingResourceException;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository; // needs contructur with final for constructor based di

    private CompanyMapper companyMapper = new CompanyMapper();

    // single cnostructor can omit @Autowired annotation
    public CompanyServiceImpl(CompanyRepository companyRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }
    @Override
    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        Company company = companyMapper.toEntity(companyDTO);
        Company savedCompany = companyRepository.save(company);

        return companyMapper.toDTO(savedCompany);
    }

    @Override
    public CompanyDTO updateCompany(Long id, CompanyDTO companyDTO) {

        // get user object from database

        Company company = companyRepository.findById(id).orElseThrow(() -> new MissingResourceException("Company not found", "Company", id + ""));

        company.setName(companyDTO.name());
        company.setEmail(companyDTO.email());

        // doesn't have id performs create and if has id than performs update operation
        Company updatedCompany = companyRepository.save(company);
        return companyMapper.toDTO(updatedCompany);
    }
}
