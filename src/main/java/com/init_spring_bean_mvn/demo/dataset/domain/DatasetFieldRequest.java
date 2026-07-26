package com.init_spring_bean_mvn.demo.dataset.domain;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record DatasetFieldRequest(
        @NotBlank @Size(max = 120) String name,
        @NotBlank @Size(max = 80) String type,
        @Size(max = 500) String description,
        @Size(max = 100) String declaredCategory,
        @Size(max = 512) String sampleHint
) {

    @Override
    public String toString() {
        return "DatasetFieldRequest[metadata_redacted=true]";
    }
}
