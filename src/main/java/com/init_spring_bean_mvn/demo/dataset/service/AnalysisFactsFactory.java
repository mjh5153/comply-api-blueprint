package com.init_spring_bean_mvn.demo.dataset.service;

import com.init_spring_bean_mvn.demo.dataset.domain.AnalysisFacts;
import com.init_spring_bean_mvn.demo.dataset.domain.ClassificationSummary;
import com.init_spring_bean_mvn.demo.dataset.domain.DataCategory;
import com.init_spring_bean_mvn.demo.dataset.domain.NormalizedDataset;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AnalysisFactsFactory {

    public AnalysisFacts from(NormalizedDataset dataset, ClassificationSummary summary) {
        Set<String> categories = summary.categories().stream()
                .map(DataCategory::value)
                .collect(Collectors.toUnmodifiableSet());

        return new AnalysisFacts(
                categories,
                Set.copyOf(dataset.jurisdictions()),
                Set.copyOf(dataset.processingPurposes()),
                Set.copyOf(dataset.processingActivities()),
                dataset.businessRole().toLowerCase(Locale.ROOT),
                dataset.retentionDays(),
                dataset.thirdPartySharing(),
                dataset.internationalTransfer(),
                dataset.automatedDecisionMaking(),
                dataset.usedForTraining(),
                dataset.usedForInference(),
                summary.hasSensitiveData(),
                summary.hasDirectIdentifier(),
                summary.has(DataCategory.PAYMENT_CARD_DATA),
                summary.has(DataCategory.HEALTH_DATA),
                summary.has(DataCategory.UNRESOLVED));
    }
}
