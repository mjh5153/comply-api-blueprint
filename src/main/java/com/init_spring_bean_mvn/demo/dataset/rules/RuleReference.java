package com.init_spring_bean_mvn.demo.dataset.rules;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RuleReference(
        String reference,
        String title,
        String reason
) {
}
