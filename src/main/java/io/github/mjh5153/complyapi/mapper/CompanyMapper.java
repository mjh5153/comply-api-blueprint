package io.github.mjh5153.complyapi.mapper;

import io.github.mjh5153.complyapi.dto.CompanyDTO;
import io.github.mjh5153.complyapi.entity.Company;
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
