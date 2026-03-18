package com.init_spring_bean_mvn.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executor;

/**
 * Async configuration for Spring Boot application.
 * Enables asynchronous method execution with custom thread pool configuration.
 *
 * Configuration Details:
 * - Core pool size: 10 threads
 * - Max pool size: 20 threads
 * - Queue capacity: 100 tasks
 * - Thread timeout: 60 seconds
 * - Thread name prefix: "Async-"
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    /**
     * Configure the async executor for handling concurrent requests
     *
     * @return Configured ThreadPoolTaskExecutor
     */
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("Async-");
        executor.setAwaitTerminationSeconds(60);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }
}

