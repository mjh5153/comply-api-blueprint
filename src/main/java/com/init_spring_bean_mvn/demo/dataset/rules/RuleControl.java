package com.init_spring_bean_mvn.demo.dataset.rules;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RuleControl(
        String control,
        String priority,
        String type,
        String reason
) {
}
