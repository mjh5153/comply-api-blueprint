package com.init_spring_bean_mvn.demo.dataset.domain;

import java.util.Set;

public record AnalysisFacts(
        Set<String> dataCategories,
        Set<String> jurisdictions,
        Set<String> processingPurposes,
        Set<String> processingActivities,
        String businessRole,
        Integer retentionDays,
        boolean thirdPartySharing,
        boolean internationalTransfer,
        boolean automatedDecisionMaking,
        boolean usedForTraining,
        boolean usedForInference,
        boolean hasSensitiveData,
        boolean hasDirectIdentifier,
        boolean hasPaymentCardData,
        boolean hasHealthData,
        boolean hasUnresolvedClassification
) {
}
