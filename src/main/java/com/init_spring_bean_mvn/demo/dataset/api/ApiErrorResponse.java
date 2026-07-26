package com.init_spring_bean_mvn.demo.dataset.api;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import java.time.Instant;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ApiErrorResponse(
        String code,
        String message,
        String correlationId,
        List<ApiFieldError> fieldErrors,
        Instant generatedAt
) {
}
