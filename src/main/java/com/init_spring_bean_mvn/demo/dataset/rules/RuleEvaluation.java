package com.init_spring_bean_mvn.demo.dataset.rules;

import com.init_spring_bean_mvn.demo.dataset.domain.Applicability;
import com.init_spring_bean_mvn.demo.dataset.domain.FrameworkType;
import com.init_spring_bean_mvn.demo.dataset.domain.RiskLevel;

import java.util.List;

public record RuleEvaluation(
        String ruleId,
        String framework,
        FrameworkType frameworkType,
        String frameworkVersion,
        Applicability applicability,
        RiskLevel risk,
        List<String> triggeringFacts,
        List<String> missingInformation,
        RuleDefinition rule
) {
}
