package com.init_spring_bean_mvn.demo.service;

import com.init_spring_bean_mvn.demo.dto.CompanyDTO;

public interface CompanyService {

    CompanyDTO createCompany(CompanyDTO companyDTO); //now need to implement in serviceImpl

    CompanyDTO updateCompany(Long id, CompanyDTO companyDTO);
}
