package com.init_spring_bean_mvn.demo.dataset.domain;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RecommendedControl(
        String control,
        ControlPriority priority,
        ControlType type,
        String reason,
        List<String> triggeringFacts,
        String ruleId
) {
}
