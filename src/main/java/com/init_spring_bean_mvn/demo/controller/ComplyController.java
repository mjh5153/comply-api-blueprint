package com.init_spring_bean_mvn.demo.controller;

import com.init_spring_bean_mvn.demo.dto.CompanyDTO;
import com.init_spring_bean_mvn.demo.service.ComplyApiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * REST Controller for COMPLY API blueprint operations.
 * Exposes endpoints for processing compliance-related company data
 * with full support for concurrent and asynchronous operations.
 *
 * Integrates with:
 * - CompanyService: Database operations
 * - AsyncHttpService: External API communication
 * - OrderFulfillmentServer: Legacy HTTP server integration
 */

/*Exception handling - @ExceptionController @ControllerAdvice
*
* Spring MVC - Populatrity -
* Clear Separation of Conerns; Dispatcher Servlet , View Resolver, View, Model
*
*
* 2. Spring Boot - Auto Configuration, Starter Dependencies, Embedded Servers, Actuator
*   A. Sprint boot vs spring and spring mvc - Spring boot is a framework built on top of spring that simplifies the development of spring applications by providing auto configuration, starter dependencies, embedded servers, and production-ready features. Spring MVC is a web framework that is part of the larger spring ecosystem and provides features for building web applications, including RESTful APIs.
*   B. @SpringBootConfiguration - includes SpringBootConfiguration, @EnableUaotConfiguration, @ComponentScan
*   C. Auto Configuration - Spring Boot automatically configures your application based on the dependencies you
*   D. Embedded Servers - Spring Boot includes embedded servers like Tomcat, Jetty, and Undertow, allowing you to run your application without needing to deploy it to an external server.
*   E. Starter Projects - Spring Boot provides starter dependencies that simplify the process of adding common libraries and frameworks to your application. For example, spring-boot-starter-web includes all the dependencies needed to build a web application.
*   F. Starter parent and DI - Spring Boot provides a starter parent POM that includes default configurations for your project. It also supports dependency injection, allowing you to manage your application's components and their dependencies easily.
*   G. Spring projects with Spring Initializr -
* start.spring.io
*   H. Configuration and application properties - Spring Boot allows you to configure your application using application.properties or application.yml files. You can also use @Configuration classes to define beans and customize your application's behavior.
*   I. Custom Application Configuration with @ConfigurationProperties -
*   J. Spring Boot Profiles - Spring Boot allows you to define different profiles for your application, such as development, testing, and production. You can use @Profile annotations to specify which beans should be loaded for each profile.
*   K. Monitoring you application with Spring boot Actuator - Spring Boot Actuator provides production-ready features for monitoring and managing your application. It includes endpoints for health checks, metrics, and more.
*   L. /application since spring boot 2.0
*   M.  Do things at startup with CommandLineRunner - Spring Boot provides the CommandLineRunner interface, which allows you to run code at startup. You can implement this interface in a bean and override the run method to execute your code when the application starts.
*   N.  Staying Relevant
*3. Spring Data, jdbc and jpa - Spring Data provides a consistent programming model for data access, whether you're using JDBC, JPA, or other data access technologies. It includes features like repositories, query methods, and support for various databases.
*   A. Spring Data JPA - Spring Data JPA is a part of the larger
*   B. jdbcTemplate.update(" ?, ? <? ", param1, param2, param3) - Spring Data JDBC provides a simple and efficient way to interact with relational databases using JDBC. It includes features like JdbcTemplate for executing SQL queries and updates, and support for transactions.
        i. RowMapper - Spring Data JDBC allows you to map rows from a ResultSet to Java objects using RowMapper. You can implement the RowMapper interface and override the mapRow method to define how the mapping should be done.
        *
    C. Hibernate - Entityt and Entity Manager - Hibernate is a popular Object-Relational Mapping (ORM) framework that provides a way to map Java objects to relational database tables. It includes features like entities, which represent database tables, and the EntityManager, which is responsible for managing the lifecycle of entities and performing database operations.
    * D. JPA Relationships - One to One, One to Many, Many to Many - JPA provides support for defining relationships between entities, such as one-to-one, one-to-many, and many-to-many relationships. You can use annotations like @OneToOne, @OneToMany, and @ManyToMany to define these relationships in your entity classes.
    * E. JPA Configuration Data Source persistence.xml and Entity Manager Factory - JPA configuration can be done using a persistence.xml file, which defines the data source, entity classes, and other settings. Alternatively, you can use Java-based configuration with @Configuration classes to set up the EntityManagerFactory and data source.
    * F. Spring Data - Need, Overview and Repository - Spring Data provides a consistent programming model for data access, making it easier to work with databases. It includes the concept of repositories, which are interfaces that define methods for performing CRUD operations and custom queries on your entities. Spring Data generates the implementation of these repositories at runtime, allowing you to focus on defining your data access logic without worrying about the underlying implementation details.
    *
    D. Unit Testing - ***
* */
@RestController
@RequestMapping("api/comply")
public class ComplyController {

    private final ComplyApiService _complyApiService;

    public ComplyController(ComplyApiService complyApiService) {
        this._complyApiService = complyApiService;
    }

    /**
     * Process a single compliance request asynchronously
     *
     * @param companyDTO Company data for compliance processing
     * @return CompletableFuture with HTTP response containing processed company
     */
    @PostMapping("/process")
    public CompletableFuture<ResponseEntity<CompanyDTO>> processCompliance(
            @RequestBody CompanyDTO companyDTO) {
        return _complyApiService.processComplianceRequest(companyDTO)
                .thenApply(processedCompany ->
                        ResponseEntity.status(HttpStatus.CREATED).body(processedCompany)
                )
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    /**
     * Process batch compliance requests in parallel
     * Handles multiple companies concurrently for bulk operations
     *
     * @param companies List of company DTOs for compliance processing
     * @return CompletableFuture with HTTP response containing processed companies
     */
    @PostMapping("/process/batch")
    public CompletableFuture<ResponseEntity<List<CompanyDTO>>> processBatchCompliance(
            @RequestBody List<CompanyDTO> companies) {
        return _complyApiService.processBatchCompliance(companies)
                .thenApply(processedCompanies ->
                        ResponseEntity.status(HttpStatus.CREATED).body(processedCompanies)
                )
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    /**
     * Send concurrent requests to external COMPLY API endpoint
     * Leverages concurrent HTTP patterns from PostConcurrentRequeststoServer
     *
     * @param apiEndpoint Target API endpoint URL
     * @param requestData Map of request parameters to send
     * @return CompletableFuture with list of API responses
     */
    @PostMapping("/external-api/concurrent")
    public CompletableFuture<ResponseEntity<List<String>>> sendConcurrentApiRequests(
            @RequestParam String apiEndpoint,
            @RequestBody Map<String, String> requestData) {
        return _complyApiService.sendConcurrentApiRequests(apiEndpoint, requestData)
                .thenApply(responses ->
                        ResponseEntity.ok().body(responses)
                )
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    /**
     * Reconcile API responses with company data
     * Processes and validates responses from external APIs
     *
     * @param apiResponses List of API response strings
     * @return CompletableFuture with reconciliation result
     */
    @PostMapping("/reconcile")
    public CompletableFuture<ResponseEntity<String>> reconcileResponses(
            @RequestBody List<String> apiResponses) {
        return _complyApiService.reconcileApiResponses(apiResponses)
                .thenApply(result ->
                        ResponseEntity.ok().body(result)
                )
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
}

