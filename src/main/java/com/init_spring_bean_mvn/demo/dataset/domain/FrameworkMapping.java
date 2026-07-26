package com.init_spring_bean_mvn.demo.dataset.domain;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record FrameworkMapping(
        String framework,
        FrameworkType frameworkType,
        String frameworkVersion,
        Applicability applicability,
        RiskLevel risk,
        List<String> ruleIds,
        List<FrameworkReference> references,
        List<String> triggeringFacts,
        List<String> missingInformation
) {
}
