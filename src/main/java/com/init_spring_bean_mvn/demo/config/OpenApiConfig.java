package com.init_spring_bean_mvn.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI 3 metadata for the generated Swagger UI at {@code /swagger-ui.html}.
 * The JSON spec is served at {@code /v3/api-docs}.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI complyApiOpenApi() {
        return new OpenAPI().info(new Info()
                .title("COMPLY API Blueprint")
                .description("Spring Boot REST API demonstrating sync + async CRUD with CompletableFuture pipelines.")
                .version("v1")
                .license(new License().name("MIT")));
    }
}

