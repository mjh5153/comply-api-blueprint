package io.github.mjh5153.complyapi.config;

import io.github.mjh5153.complyapi.entity.Company;
import io.github.mjh5153.complyapi.repository.CompanyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Seeds a set of sample companies on application startup.
 *
 * <p>The default profile runs against an in-memory H2 database that starts
 * empty on every boot, which leaves the comply-ui companies view showing
 * "No companies yet". This seeder populates a handful of realistic records
 * so the view has data to display out of the box.
 *
 * <p>Seeding is idempotent: it only runs when the {@code companys} table is
 * empty, so it never duplicates rows or clobbers companies created at runtime
 * (including against a persistent MySQL datasource).
 */
@Configuration
public class DataSeeder {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    /**
     * Sample companies used to seed an empty datastore. Emails are unique so
     * they satisfy the {@code email} unique constraint on {@link Company}.
     */
    private static final List<Company> SAMPLE_COMPANIES = List.of(
            new Company(null, "Acme Corporation", "ops@acme.test"),
            new Company(null, "Globex Corporation", "compliance@globex.test"),
            new Company(null, "Initech", "audit@initech.test"),
            new Company(null, "Umbrella Industries", "risk@umbrella.test"),
            new Company(null, "Soylent Foods", "legal@soylent.test"),
            new Company(null, "Stark Industries", "governance@stark.test"),
            new Company(null, "Wayne Enterprises", "security@wayne.test"),
            new Company(null, "Wonka Manufacturing", "quality@wonka.test")
    );

    @Bean
    public CommandLineRunner seedCompanies(CompanyRepository companyRepository) {
        return args -> {
            if (companyRepository.count() > 0) {
                log.info("Company data already present ({} rows); skipping seed.",
                        companyRepository.count());
                return;
            }
            companyRepository.saveAll(SAMPLE_COMPANIES);
            log.info("Seeded {} sample companies into an empty datastore.",
                    SAMPLE_COMPANIES.size());
        };
    }
}
