package com.init_spring_bean_mvn.demo.dataset.service;

import com.init_spring_bean_mvn.demo.dataset.domain.AnalysisFacts;
import com.init_spring_bean_mvn.demo.dataset.domain.Applicability;
import com.init_spring_bean_mvn.demo.dataset.domain.ClassificationResult;
import com.init_spring_bean_mvn.demo.dataset.domain.ClassificationSummary;
import com.init_spring_bean_mvn.demo.dataset.domain.ControlPriority;
import com.init_spring_bean_mvn.demo.dataset.domain.ControlType;
import com.init_spring_bean_mvn.demo.dataset.domain.DatasetAnalysisRequest;
import com.init_spring_bean_mvn.demo.dataset.domain.DatasetAnalysisResponse;
import com.init_spring_bean_mvn.demo.dataset.domain.FrameworkMapping;
import com.init_spring_bean_mvn.demo.dataset.domain.FrameworkReference;
import com.init_spring_bean_mvn.demo.dataset.domain.FrameworkType;
import com.init_spring_bean_mvn.demo.dataset.domain.RecommendedControl;
import com.init_spring_bean_mvn.demo.dataset.domain.RiskAssessment;
import com.init_spring_bean_mvn.demo.dataset.domain.RiskLevel;
import com.init_spring_bean_mvn.demo.dataset.domain.NormalizedDataset;
import com.init_spring_bean_mvn.demo.dataset.rules.RuleControl;
import com.init_spring_bean_mvn.demo.dataset.rules.RuleEvaluation;
import com.init_spring_bean_mvn.demo.dataset.rules.RuleEvaluationEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DatasetAnalysisService {

    public static final String API_VERSION = "v1";
    public static final String ENGINE_VERSION = "0.1.0";
    private static final Logger LOGGER = LoggerFactory.getLogger(DatasetAnalysisService.class);

    private final DatasetNormalizer normalizer;
    private final DeterministicFieldClassifier classifier;
    private final AnalysisFactsFactory factsFactory;
    private final RuleEvaluationEngine ruleEngine;
    private final RiskAssessmentService riskAssessmentService;
    private final Clock clock;

    @Autowired
    public DatasetAnalysisService(
            DatasetNormalizer normalizer,
            DeterministicFieldClassifier classifier,
            AnalysisFactsFactory factsFactory,
            RuleEvaluationEngine ruleEngine,
            RiskAssessmentService riskAssessmentService) {
        this(normalizer, classifier, factsFactory, ruleEngine, riskAssessmentService, Clock.systemUTC());
    }

    DatasetAnalysisService(
            DatasetNormalizer normalizer,
            DeterministicFieldClassifier classifier,
            AnalysisFactsFactory factsFactory,
            RuleEvaluationEngine ruleEngine,
            RiskAssessmentService riskAssessmentService,
            Clock clock) {
        this.normalizer = normalizer;
        this.classifier = classifier;
        this.factsFactory = factsFactory;
        this.ruleEngine = ruleEngine;
        this.riskAssessmentService = riskAssessmentService;
        this.clock = clock;
    }

    public DatasetAnalysisResponse analyze(DatasetAnalysisRequest request, String correlationId) {
        long started = System.nanoTime();
        NormalizedDataset dataset = normalizer.normalize(request);
        ClassificationSummary classification = classifier.classify(dataset.fields());
        AnalysisFacts facts = factsFactory.from(dataset, classification);
        List<RuleEvaluation> evaluations = ruleEngine.evaluate(dataset, classification);
        RiskAssessment risk = riskAssessmentService.assess(dataset, classification, facts, evaluations);
        String scanId = "scan_" + dataset.fingerprint().substring(0, 24);

        List<String> assumptions = assumptions(request, dataset, classification);
        List<String> warnings = warnings(classification);
        DatasetAnalysisResponse response = new DatasetAnalysisResponse(
                scanId,
                dataset.datasetName(),
                risk.level(),
                risk.factors(),
                classification.results(),
                frameworkMappings(evaluations),
                controls(evaluations),
                assumptions,
                warnings,
                API_VERSION,
                ENGINE_VERSION,
                ruleEngine.ruleSetVersion(),
                correlationId,
                Instant.now(clock));

        LOGGER.atInfo()
                .addKeyValue("scan_id", scanId)
                .addKeyValue("correlation_id", correlationId)
                .addKeyValue("field_count", dataset.fields().size())
                .addKeyValue("classification_count", classification.results().size())
                .addKeyValue("matched_rule_count", evaluations.size())
                .addKeyValue("risk", risk.level().value())
                .addKeyValue("duration_ms", (System.nanoTime() - started) / 1_000_000)
                .log("Dataset analysis completed");
        return response;
    }

    private List<FrameworkMapping> frameworkMappings(List<RuleEvaluation> evaluations) {
        return evaluations.stream()
                .collect(Collectors.groupingBy(RuleEvaluation::framework, LinkedHashMap::new, Collectors.toList()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    List<RuleEvaluation> rules = entry.getValue();
                    Applicability applicability = rules.stream()
                            .map(RuleEvaluation::applicability)
                            .reduce(Applicability.NOT_APPLICABLE, Applicability::highest);
                    RiskLevel risk = rules.stream()
                            .map(RuleEvaluation::risk)
                            .reduce(RiskLevel.LOW, RiskLevel::highest);
                    List<FrameworkReference> references = rules.stream()
                            .flatMap(rule -> rule.rule().then().references().stream()
                                    .map(reference -> new FrameworkReference(
                                            reference.reference(),
                                            reference.title(),
                                            reference.reason(),
                                            rule.triggeringFacts(),
                                            rule.ruleId())))
                            .sorted(Comparator.comparing(FrameworkReference::reference)
                                    .thenComparing(FrameworkReference::ruleId))
                            .toList();
                    return new FrameworkMapping(
                            entry.getKey(),
                            rules.get(0).frameworkType(),
                            rules.get(0).frameworkVersion(),
                            applicability,
                            risk,
                            rules.stream().map(RuleEvaluation::ruleId).sorted().toList(),
                            references,
                            rules.stream().flatMap(rule -> rule.triggeringFacts().stream()).distinct().sorted().toList(),
                            rules.stream().flatMap(rule -> rule.missingInformation().stream()).distinct().sorted().toList());
                })
                .toList();
    }

    private List<RecommendedControl> controls(List<RuleEvaluation> evaluations) {
        Map<String, RecommendedControl> controls = new LinkedHashMap<>();
        for (RuleEvaluation evaluation : evaluations) {
            for (RuleControl control : safeControls(evaluation)) {
                ControlPriority priority = parsePriority(control.priority());
                ControlType type = parseType(control.type());
                RecommendedControl candidate = new RecommendedControl(
                        control.control(),
                        priority,
                        type,
                        control.reason(),
                        evaluation.triggeringFacts(),
                        evaluation.ruleId());
                RecommendedControl previous = controls.get(control.control());
                if (previous == null || priority.rank() > previous.priority().rank()) {
                    controls.put(control.control(), candidate);
                } else if (previous != null) {
                    List<String> facts = new LinkedHashSet<>(previous.triggeringFacts()).stream()
                            .collect(Collectors.toCollection(ArrayList::new));
                    facts.addAll(evaluation.triggeringFacts());
                    controls.put(control.control(), new RecommendedControl(
                            previous.control(),
                            previous.priority(),
                            previous.type(),
                            previous.reason(),
                            facts.stream().distinct().sorted().toList(),
                            previous.ruleId()));
                }
            }
        }
        return controls.values().stream()
                .sorted(Comparator.comparing(RecommendedControl::priority, Comparator.comparingInt(ControlPriority::rank).reversed())
                        .thenComparing(RecommendedControl::control))
                .toList();
    }

    private List<RuleControl> safeControls(RuleEvaluation evaluation) {
        return evaluation.rule().then().recommendedControls() == null
                ? List.of() : evaluation.rule().then().recommendedControls();
    }

    private ControlPriority parsePriority(String value) {
        return ControlPriority.valueOf(value.trim().toUpperCase());
    }

    private ControlType parseType(String value) {
        return ControlType.valueOf(value.trim().toUpperCase());
    }

    private List<String> assumptions(
            DatasetAnalysisRequest request,
            NormalizedDataset dataset,
            ClassificationSummary classification) {
        List<String> assumptions = new ArrayList<>();
        if (request.retentionDays() == null) {
            assumptions.add("Retention period was not supplied; retention rules requiring a numeric value were not evaluated.");
        }
        if (request.thirdPartySharing() == null || request.internationalTransfer() == null
                || request.automatedDecisionMaking() == null || request.usedForTraining() == null
                || request.usedForInference() == null) {
            assumptions.add("Unspecified optional processing flags were treated as false for deterministic evaluation.");
        }
        if (classification.unresolvedFields().isEmpty()) {
            return assumptions.stream().sorted().toList();
        }
        assumptions.add("Unresolved fields require additional semantic metadata or analyst review.");
        return assumptions.stream().sorted().toList();
    }

    private List<String> warnings(ClassificationSummary classification) {
        List<String> warnings = new ArrayList<>();
        warnings.add("This result is compliance-analysis support and not legal advice.");
        warnings.add("Framework mappings are indicators based on supplied metadata, not final legal determinations.");
        if (!classification.unresolvedFields().isEmpty()) {
            warnings.add("One or more fields could not be classified deterministically; review unresolved fields before relying on the result.");
        }
        return warnings;
    }
}
