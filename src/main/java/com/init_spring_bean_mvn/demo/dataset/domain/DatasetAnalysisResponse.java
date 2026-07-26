package com.init_spring_bean_mvn.demo.dataset.domain;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import java.time.Instant;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record DatasetAnalysisResponse(
        String scanId,
        String datasetName,
        RiskLevel overallRisk,
        List<RiskFactor> riskFactors,
        List<ClassificationResult> detectedDataCategories,
        List<FrameworkMapping> applicableFrameworks,
        List<RecommendedControl> recommendedControls,
        List<String> assumptions,
        List<String> warnings,
        String apiVersion,
        String engineVersion,
        String ruleSetVersion,
        String correlationId,
        Instant generatedAt
) {
}
