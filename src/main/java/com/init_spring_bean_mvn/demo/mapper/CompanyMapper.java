package com.init_spring_bean_mvn.demo.mapper;

import com.init_spring_bean_mvn.demo.dto.CompanyDTO;
import com.init_spring_bean_mvn.demo.entity.Company;
import org.springframework.stereotype.Component;

@Component // marks class as spring bean
public class CompanyMapper {
    public CompanyDTO toDTO(Company company) {
        return new CompanyDTO(
                company.getId(), company.getName(), company.getEmail()
        );
    }

    public Company toEntity(CompanyDTO companyDTO) {
        return new Company(
                companyDTO.id(),
                companyDTO.name(),
                companyDTO.email()
        );
    }
}
