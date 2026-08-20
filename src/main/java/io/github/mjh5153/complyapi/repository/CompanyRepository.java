package io.github.mjh5153.complyapi.repository;

import io.github.mjh5153.complyapi.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data repository for {@link Company} entities.
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByEmail(String email);
}
