package com.init_spring_bean_mvn.demo.dataset.rules;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RuleCondition(
        List<String> anyDataCategories,
        List<String> jurisdictions,
        List<String> anyProcessingPurposes,
        List<String> anyProcessingActivities,
        List<String> anyBusinessRoles,
        Boolean hasSensitiveData,
        Boolean hasDirectIdentifier,
        Boolean hasPaymentCardData,
        Boolean hasHealthData,
        Boolean hasUnresolvedClassification,
        Boolean thirdPartySharing,
        Boolean internationalTransfer,
        Boolean usedForTraining,
        Boolean usedForInference,
        Boolean automatedDecisionMaking,
        Integer retentionDaysGreaterThan
) {
}
