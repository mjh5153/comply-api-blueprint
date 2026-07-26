package com.init_spring_bean_mvn.demo.dataset;

import com.init_spring_bean_mvn.demo.dataset.domain.ClassificationSummary;
import com.init_spring_bean_mvn.demo.dataset.domain.DataCategory;
import com.init_spring_bean_mvn.demo.dataset.domain.DatasetAnalysisRequest;
import com.init_spring_bean_mvn.demo.dataset.domain.DatasetFieldRequest;
import com.init_spring_bean_mvn.demo.dataset.service.DatasetNormalizer;
import com.init_spring_bean_mvn.demo.dataset.service.DeterministicFieldClassifier;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DeterministicFieldClassifierTest {

    private final DatasetNormalizer normalizer = new DatasetNormalizer();
    private final DeterministicFieldClassifier classifier = new DeterministicFieldClassifier();

    @Test
    void classifiesObviousFieldsWithoutAnAiDependency() {
        ClassificationSummary result = classifier.classify(normalizer.normalize(new DatasetAnalysisRequest(
                "test", List.of("EU"), "controller", List.of("analytics"), List.of("storage"), null,
                false, false, false, false, false, List.of(
                new DatasetFieldRequest("email", "string", null, null, null),
                new DatasetFieldRequest("ip_address", "string", null, null, "192.0.2.10"),
                new DatasetFieldRequest("amount", "decimal", null, null, null)))).fields());

        assertThat(result.categories()).contains(
                DataCategory.CONTACT_INFORMATION,
                DataCategory.ONLINE_IDENTIFIER,
                DataCategory.FINANCIAL_DATA);
        assertThat(result.categories()).doesNotContain(DataCategory.POSTAL_ADDRESS, DataCategory.TELEPHONE_NUMBER);
        assertThat(result.unresolvedFields()).isEmpty();
    }

    @Test
    void leavesAmbiguousFieldsUnresolved() {
        ClassificationSummary result = classifier.classify(normalizer.normalize(new DatasetAnalysisRequest(
                "test", List.of("EU"), "controller", List.of("analytics"), List.of("storage"), null,
                false, false, false, false, false, List.of(
                new DatasetFieldRequest("value", "string", null, null, null),
                new DatasetFieldRequest("reference", "string", null, null, null)))).fields());

        assertThat(result.categories()).contains(DataCategory.UNRESOLVED);
        assertThat(result.unresolvedFields()).containsExactly("reference", "value");
    }
}
