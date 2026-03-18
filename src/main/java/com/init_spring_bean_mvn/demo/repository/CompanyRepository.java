package com.init_spring_bean_mvn.demo.repository;

import com.init_spring_bean_mvn.demo.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface CompanyRepository extends JpaRepository<Company, Long> {
}
