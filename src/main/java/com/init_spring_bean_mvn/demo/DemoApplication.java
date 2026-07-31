package com.init_spring_bean_mvn.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application entry point for the COMPLY API Blueprint service.
 *
 * <p>Bootstraps a Spring Boot application that exposes:
 * <ul>
 *   <li>Synchronous and asynchronous CRUD endpoints for {@code Company}
 *       (see {@code CompanyController}).</li>
 *   <li>COMPLY compliance-processing endpoints backed by concurrent
 *       {@link java.util.concurrent.CompletableFuture} pipelines
 *       (see {@code ComplyController}).</li>
 * </ul>
 *
 * <p>The default profile uses an in-memory H2 database so the application
 * runs without any external setup. Activate the {@code mysql} profile
 * (e.g. {@code --spring.profiles.active=mysql}) to switch to MySQL.
 */
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
