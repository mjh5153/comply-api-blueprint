package com.init_spring_bean_mvn.demo.dataset.rules;

import com.init_spring_bean_mvn.demo.dataset.domain.AnalysisFacts;
import com.init_spring_bean_mvn.demo.dataset.domain.Applicability;
import com.init_spring_bean_mvn.demo.dataset.domain.ClassificationSummary;
import com.init_spring_bean_mvn.demo.dataset.domain.FrameworkType;
import com.init_spring_bean_mvn.demo.dataset.domain.NormalizedDataset;
import com.init_spring_bean_mvn.demo.dataset.domain.RiskLevel;
import com.init_spring_bean_mvn.demo.dataset.service.AnalysisFactsFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RuleEvaluationEngine {

    private final YamlRuleCatalogLoader catalogLoader;
    private final AnalysisFactsFactory factsFactory;

    public RuleEvaluationEngine(YamlRuleCatalogLoader catalogLoader, AnalysisFactsFactory factsFactory) {
        this.catalogLoader = catalogLoader;
        this.factsFactory = factsFactory;
    }

    public List<RuleEvaluation> evaluate(NormalizedDataset dataset, ClassificationSummary summary) {
        AnalysisFacts facts = factsFactory.from(dataset, summary);
        return catalogLoader.catalog().rules().stream()
                .filter(rule -> matches(rule.when(), facts))
                .map(rule -> new RuleEvaluation(
                        rule.id(),
                        rule.framework(),
                        parseFrameworkType(rule.frameworkType()),
                        rule.frameworkVersion(),
                        parseApplicability(rule.then().applicability()),
                        parseRisk(rule.then().risk()),
                        triggeringFacts(rule.when(), facts),
                        safeList(rule.then().missingInformation()),
                        rule))
                .sorted(Comparator.comparing(RuleEvaluation::framework)
                        .thenComparing(RuleEvaluation::ruleId))
                .toList();
    }

    public String ruleSetVersion() {
        return catalogLoader.catalog().version();
    }

    private boolean matches(RuleCondition condition, AnalysisFacts facts) {
        if (condition == null) {
            return false;
        }
        return intersects(condition.anyDataCategories(), facts.dataCategories(), false)
                && intersects(condition.jurisdictions(), facts.jurisdictions(), true)
                && intersects(condition.anyProcessingPurposes(), facts.processingPurposes(), false)
                && intersects(condition.anyProcessingActivities(), facts.processingActivities(), false)
                && intersects(condition.anyBusinessRoles(), Set.of(facts.businessRole()), false)
                && matches(condition.hasSensitiveData(), facts.hasSensitiveData())
                && matches(condition.hasDirectIdentifier(), facts.hasDirectIdentifier())
                && matches(condition.hasPaymentCardData(), facts.hasPaymentCardData())
                && matches(condition.hasHealthData(), facts.hasHealthData())
                && matches(condition.hasUnresolvedClassification(), facts.hasUnresolvedClassification())
                && matches(condition.thirdPartySharing(), facts.thirdPartySharing())
                && matches(condition.internationalTransfer(), facts.internationalTransfer())
                && matches(condition.usedForTraining(), facts.usedForTraining())
                && matches(condition.usedForInference(), facts.usedForInference())
                && matches(condition.automatedDecisionMaking(), facts.automatedDecisionMaking())
                && matchesRetention(condition.retentionDaysGreaterThan(), facts.retentionDays());
    }

    private boolean intersects(List<String> expected, Set<String> actual, boolean jurisdiction) {
        if (expected == null || expected.isEmpty()) {
            return true;
        }
        if (jurisdiction && expected.stream().filter(Objects::nonNull)
                .map(this::normalizeJurisdiction).anyMatch("GLOBAL"::equals)) {
            return true;
        }
        return expected.stream()
                .filter(Objects::nonNull)
                .map(value -> jurisdiction ? normalizeJurisdiction(value) : normalize(value))
                .anyMatch(actual::contains);
    }

    private boolean matches(Boolean expected, boolean actual) {
        return expected == null || expected == actual;
    }

    private boolean matchesRetention(Integer minimumExclusive, Integer actual) {
        return minimumExclusive == null || actual != null && actual > minimumExclusive;
    }

    private List<String> triggeringFacts(RuleCondition condition, AnalysisFacts facts) {
        List<String> result = new ArrayList<>();
        if (condition.anyDataCategories() != null && !condition.anyDataCategories().isEmpty()) {
            result.add("data_categories=" + sortedIntersection(condition.anyDataCategories(), facts.dataCategories(), false));
        }
        if (condition.jurisdictions() != null && !condition.jurisdictions().isEmpty()) {
            result.add("jurisdictions=" + sortedIntersection(condition.jurisdictions(), facts.jurisdictions(), true));
        }
        if (condition.anyProcessingPurposes() != null && !condition.anyProcessingPurposes().isEmpty()) {
            result.add("processing_purposes=" + sortedIntersection(condition.anyProcessingPurposes(), facts.processingPurposes(), false));
        }
        if (condition.anyProcessingActivities() != null && !condition.anyProcessingActivities().isEmpty()) {
            result.add("processing_activities=" + sortedIntersection(condition.anyProcessingActivities(), facts.processingActivities(), false));
        }
        if (condition.anyBusinessRoles() != null && !condition.anyBusinessRoles().isEmpty()) {
            result.add("business_role=" + facts.businessRole());
        }
        addBooleanFact(result, "has_sensitive_data", condition.hasSensitiveData(), facts.hasSensitiveData());
        addBooleanFact(result, "has_direct_identifier", condition.hasDirectIdentifier(), facts.hasDirectIdentifier());
        addBooleanFact(result, "has_payment_card_data", condition.hasPaymentCardData(), facts.hasPaymentCardData());
        addBooleanFact(result, "has_health_data", condition.hasHealthData(), facts.hasHealthData());
        addBooleanFact(result, "has_unresolved_classification", condition.hasUnresolvedClassification(), facts.hasUnresolvedClassification());
        addBooleanFact(result, "third_party_sharing", condition.thirdPartySharing(), facts.thirdPartySharing());
        addBooleanFact(result, "international_transfer", condition.internationalTransfer(), facts.internationalTransfer());
        addBooleanFact(result, "used_for_training", condition.usedForTraining(), facts.usedForTraining());
        addBooleanFact(result, "used_for_inference", condition.usedForInference(), facts.usedForInference());
        addBooleanFact(result, "automated_decision_making", condition.automatedDecisionMaking(), facts.automatedDecisionMaking());
        if (condition.retentionDaysGreaterThan() != null && facts.retentionDays() != null) {
            result.add("retention_days=" + facts.retentionDays());
        }
        return result.stream().sorted().toList();
    }

    private void addBooleanFact(List<String> facts, String name, Boolean expected, boolean actual) {
        if (expected != null && expected == actual) {
            facts.add(name + "=" + actual);
        }
    }

    private String sortedIntersection(List<String> expected, Set<String> actual, boolean jurisdiction) {
        if (jurisdiction && expected.stream()
                .filter(Objects::nonNull)
                .map(this::normalizeJurisdiction)
                .anyMatch("GLOBAL"::equals)) {
            return "GLOBAL";
        }
        return expected.stream()
                .filter(Objects::nonNull)
                .map(value -> jurisdiction ? normalizeJurisdiction(value) : normalize(value))
                .filter(actual::contains)
                .sorted()
                .collect(Collectors.joining(","));
    }

    private FrameworkType parseFrameworkType(String value) {
        return FrameworkType.valueOf(value.trim().toUpperCase(Locale.ROOT));
    }

    private Applicability parseApplicability(String value) {
        return Applicability.valueOf(value.trim().toUpperCase(Locale.ROOT));
    }

    private RiskLevel parseRisk(String value) {
        return RiskLevel.valueOf(value.trim().toUpperCase(Locale.ROOT));
    }

    private List<String> safeList(List<String> values) {
        return values == null ? List.of() : values.stream().filter(Objects::nonNull).sorted().toList();
    }

    private String normalize(String value) {
        return value.trim().toLowerCase(Locale.ROOT)
                .replace('-', '_')
                .replace(' ', '_');
    }

    private String normalizeJurisdiction(String value) {
        String normalized = normalize(value);
        return switch (normalized) {
            case "us_ca", "california", "ca" -> "US-CA";
            case "us", "usa", "united_states" -> "US";
            case "eu", "europe", "european_union" -> "EU";
            default -> normalized.toUpperCase(Locale.ROOT);
        };
    }
}
