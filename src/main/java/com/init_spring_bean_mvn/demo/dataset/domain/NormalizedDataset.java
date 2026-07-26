package com.init_spring_bean_mvn.demo.dataset.domain;

import java.util.List;

public record NormalizedDataset(
        String datasetName,
        List<String> jurisdictions,
        String businessRole,
        List<String> processingPurposes,
        List<String> processingActivities,
        Integer retentionDays,
        boolean thirdPartySharing,
        boolean internationalTransfer,
        boolean automatedDecisionMaking,
        boolean usedForTraining,
        boolean usedForInference,
        List<NormalizedField> fields,
        String fingerprint
) {
}
