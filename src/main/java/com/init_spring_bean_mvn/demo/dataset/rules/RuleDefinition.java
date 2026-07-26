package com.init_spring_bean_mvn.demo.dataset.rules;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDate;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RuleDefinition(
        String id,
        String version,
        String framework,
        String frameworkVersion,
        String frameworkType,
        List<String> jurisdictions,
        String status,
        LocalDate effectiveFrom,
        RuleCondition when,
        RuleAction then,
        String rationale,
        RuleSource source
) {
}
