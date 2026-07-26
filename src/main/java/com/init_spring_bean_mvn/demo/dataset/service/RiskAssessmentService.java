package com.init_spring_bean_mvn.demo.dataset.service;

import com.init_spring_bean_mvn.demo.dataset.domain.AnalysisFacts;
import com.init_spring_bean_mvn.demo.dataset.domain.ClassificationSummary;
import com.init_spring_bean_mvn.demo.dataset.domain.DataCategory;
import com.init_spring_bean_mvn.demo.dataset.domain.NormalizedDataset;
import com.init_spring_bean_mvn.demo.dataset.domain.RiskAssessment;
import com.init_spring_bean_mvn.demo.dataset.domain.RiskFactor;
import com.init_spring_bean_mvn.demo.dataset.domain.RiskLevel;
import com.init_spring_bean_mvn.demo.dataset.rules.RuleEvaluation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class RiskAssessmentService {

    public RiskAssessment assess(
            NormalizedDataset dataset,
            ClassificationSummary summary,
            AnalysisFacts facts,
            List<RuleEvaluation> evaluations) {
        List<RiskFactor> factors = new ArrayList<>();

        if (facts.hasDirectIdentifier()) {
            factors.add(new RiskFactor("direct_identifiers", 2,
                    "The dataset contains fields that can identify or single out people or devices."));
        }
        if (facts.hasSensitiveData()) {
            factors.add(new RiskFactor("sensitive_data", 3,
                    "The dataset contains health, biometric, payment, authentication, government, or children's data indicators."));
        }
        if (facts.hasPaymentCardData()) {
            factors.add(new RiskFactor("payment_card_data", 3,
                    "Payment-card data creates elevated security and industry-standard scope concerns."));
        }
        if (facts.hasHealthData()) {
            factors.add(new RiskFactor("health_data", 2,
                    "Health-data indicators require additional entity and purpose analysis."));
        }
        if (summary.has(DataCategory.CHILDRENS_DATA)) {
            factors.add(new RiskFactor("childrens_data", 3,
                    "Children's data indicators increase the need for age, consent, and safeguards analysis."));
        }
        if (summary.has(DataCategory.AUTHENTICATION_CREDENTIALS)) {
            factors.add(new RiskFactor("authentication_credentials", 3,
                    "Credentials and secrets require strong access, storage, and rotation controls."));
        }
        if (summary.has(DataCategory.FINANCIAL_DATA)) {
            factors.add(new RiskFactor("financial_data", 1,
                    "Financial transaction fields warrant purpose, retention, and security review."));
        }
        if (summary.has(DataCategory.FREE_FORM_TEXT)) {
            factors.add(new RiskFactor("free_form_text", 1,
                    "Free-form text may contain unexpected personal or sensitive information and needs content-governance review."));
        }
        if (dataset.processingPurposes().stream().anyMatch(this::isHighRiskPurpose)) {
            factors.add(new RiskFactor("high_risk_processing_purpose", 2,
                    "The declared purpose may involve elevated monitoring, fraud, marketing, or model risk."));
        }
        if (facts.automatedDecisionMaking()) {
            factors.add(new RiskFactor("automated_decision_making", 2,
                    "Automated decisions require system-level impact and governance analysis."));
        }
        if (facts.usedForTraining() || facts.usedForInference()) {
            factors.add(new RiskFactor("ai_processing", 1,
                    "Training or inference use requires additional data-governance and AI-system context."));
        }
        if (facts.retentionDays() != null && facts.retentionDays() > 365) {
            factors.add(new RiskFactor("long_retention", 1,
                    "The declared retention period is longer than one year and should be justified."));
        }
        if (facts.thirdPartySharing()) {
            factors.add(new RiskFactor("third_party_sharing", 1,
                    "Third-party sharing requires recipient, purpose, contract, and transfer review."));
        }
        if (facts.internationalTransfer()) {
            factors.add(new RiskFactor("international_transfer", 1,
                    "International transfer metadata requires destination and transfer-mechanism review."));
        }
        if (facts.hasUnresolvedClassification()) {
            factors.add(new RiskFactor("unresolved_classification", 1,
                    "One or more fields could not be classified from the supplied evidence."));
        }
        if (dataset.jurisdictions().size() > 1) {
            factors.add(new RiskFactor("multiple_jurisdictions", 1,
                    "Multiple jurisdictions increase the need for conflict and transfer analysis."));
        }
        evaluations.stream()
                .filter(evaluation -> evaluation.risk() == RiskLevel.HIGH || evaluation.risk() == RiskLevel.CRITICAL)
                .map(RuleEvaluation::framework)
                .distinct()
                .sorted()
                .forEach(framework -> factors.add(new RiskFactor(
                        "elevated_framework_signal:" + framework,
                        1,
                        "At least one matched rule for this framework has elevated review risk.")));

        List<RiskFactor> sortedFactors = factors.stream()
                .sorted(Comparator.comparingInt(RiskFactor::points).reversed()
                        .thenComparing(RiskFactor::factor))
                .toList();
        int score = sortedFactors.stream().mapToInt(RiskFactor::points).sum();
        RiskLevel level = score >= 8 ? RiskLevel.CRITICAL
                : score >= 5 ? RiskLevel.HIGH
                : score >= 2 ? RiskLevel.MEDIUM
                : RiskLevel.LOW;
        return new RiskAssessment(level, sortedFactors);
    }

    private boolean isHighRiskPurpose(String purpose) {
        return purpose.equals("fraud_detection")
                || purpose.equals("marketing")
                || purpose.equals("surveillance")
                || purpose.equals("model_training")
                || purpose.equals("ai_training")
                || purpose.equals("automated_decision_making");
    }
}
