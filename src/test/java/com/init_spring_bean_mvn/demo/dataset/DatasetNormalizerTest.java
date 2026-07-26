package com.init_spring_bean_mvn.demo.dataset;

import com.init_spring_bean_mvn.demo.dataset.domain.DatasetAnalysisRequest;
import com.init_spring_bean_mvn.demo.dataset.domain.DatasetFieldRequest;
import com.init_spring_bean_mvn.demo.dataset.service.DatasetNormalizer;
import com.init_spring_bean_mvn.demo.dataset.service.DatasetValidationException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;

class DatasetNormalizerTest {

    private final DatasetNormalizer normalizer = new DatasetNormalizer();

    @Test
    void equivalentMetadataProducesTheSameFingerprint() {
        DatasetAnalysisRequest first = request(
                " Customer Transactions ",
                List.of("EU", "US-CA"),
                List.of(field("Email", "String"), field("amount", "Decimal")));
        DatasetAnalysisRequest reordered = request(
                "Customer Transactions",
                List.of("california", "europe"),
                List.of(field("amount", "decimal"), field("email", "string")));

        assertEquals(normalizer.normalize(first).fingerprint(), normalizer.normalize(reordered).fingerprint());
        assertEquals(List.of("EU", "US-CA"), normalizer.normalize(reordered).jurisdictions());
    }

    @Test
    void duplicateFieldNamesAfterNormalizationAreRejected() {
        DatasetValidationException exception = assertThrows(DatasetValidationException.class,
                () -> normalizer.normalize(request(
                        "DuplicateFields",
                        List.of("US"),
                        List.of(field("customer-id", "string"), field("customer id", "string")))));

        assertEquals("Dataset contains duplicate field names after normalization", exception.getMessage());
        assertEquals(1, exception.fieldErrors().size());
    }

    @Test
    void requestToStringDoesNotExposeUserSuppliedMetadata() {
        DatasetAnalysisRequest request = request(
                "CustomerTransactions",
                List.of("EU"),
                List.of(new DatasetFieldRequest(
                        "email",
                        "string",
                        "Private customer contact",
                        null,
                        "person@example.com")));

        assertFalse(request.toString().contains("person@example.com"));
        assertFalse(request.toString().contains("Private customer contact"));
        assertFalse(request.toString().contains("email"));
    }

    private DatasetAnalysisRequest request(String name, List<String> jurisdictions, List<DatasetFieldRequest> fields) {
        return new DatasetAnalysisRequest(
                name,
                jurisdictions,
                "controller",
                List.of("analytics"),
                List.of("storage"),
                null,
                null,
                null,
                null,
                null,
                null,
                fields);
    }

    private DatasetFieldRequest field(String name, String type) {
        return new DatasetFieldRequest(name, type, null, null, null);
    }
}
