package com.init_spring_bean_mvn.demo.dataset.service;

import com.init_spring_bean_mvn.demo.dataset.api.ApiFieldError;
import com.init_spring_bean_mvn.demo.dataset.domain.DatasetAnalysisRequest;
import com.init_spring_bean_mvn.demo.dataset.domain.DatasetFieldRequest;
import com.init_spring_bean_mvn.demo.dataset.domain.NormalizedDataset;
import com.init_spring_bean_mvn.demo.dataset.domain.NormalizedField;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HexFormat;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DatasetNormalizer {

    public NormalizedDataset normalize(DatasetAnalysisRequest request) {
        List<NormalizedField> fields = request.fields().stream()
                .map(this::normalizeField)
                .sorted(Comparator.comparing(NormalizedField::normalizedName)
                        .thenComparing(NormalizedField::originalName))
                .toList();

        List<ApiFieldError> errors = duplicateFieldErrors(fields);
        if (!errors.isEmpty()) {
            throw new DatasetValidationException("Dataset contains duplicate field names after normalization", errors);
        }

        List<String> jurisdictions = normalizeList(request.jurisdictions()).stream()
                .map(this::normalizeJurisdiction)
                .distinct()
                .sorted()
                .toList();

        String canonical = String.join("|",
                normalizeText(request.datasetName()),
                String.join(",", jurisdictions),
                normalizeToken(request.businessRole()),
                String.join(",", normalizeList(request.processingPurposes())),
                String.join(",", normalizeList(request.processingActivities())),
                String.valueOf(request.retentionDays()),
                String.valueOf(Boolean.TRUE.equals(request.thirdPartySharing())),
                String.valueOf(Boolean.TRUE.equals(request.internationalTransfer())),
                String.valueOf(Boolean.TRUE.equals(request.automatedDecisionMaking())),
                String.valueOf(Boolean.TRUE.equals(request.usedForTraining())),
                String.valueOf(Boolean.TRUE.equals(request.usedForInference())),
                fields.stream().map(this::canonicalField).collect(Collectors.joining(",")));

        return new NormalizedDataset(
                normalizeText(request.datasetName()),
                jurisdictions,
                normalizeToken(request.businessRole()),
                normalizeList(request.processingPurposes()),
                normalizeList(request.processingActivities()),
                request.retentionDays(),
                Boolean.TRUE.equals(request.thirdPartySharing()),
                Boolean.TRUE.equals(request.internationalTransfer()),
                Boolean.TRUE.equals(request.automatedDecisionMaking()),
                Boolean.TRUE.equals(request.usedForTraining()),
                Boolean.TRUE.equals(request.usedForInference()),
                fields,
                sha256(canonical));
    }

    public static String normalizeToken(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "_")
                .replaceAll("^_+|_+$", "")
                .replaceAll("_+", "_");
    }

    private NormalizedField normalizeField(DatasetFieldRequest field) {
        return new NormalizedField(
                field.name().trim(),
                normalizeToken(field.name()),
                normalizeToken(field.type()),
                normalizeOptional(field.description()),
                normalizeOptional(field.declaredCategory()),
                normalizeOptional(field.sampleHint()));
    }

    private List<ApiFieldError> duplicateFieldErrors(List<NormalizedField> fields) {
        Set<String> seen = new LinkedHashSet<>();
        List<ApiFieldError> errors = new ArrayList<>();
        for (NormalizedField field : fields) {
            if (!seen.add(field.normalizedName())) {
                errors.add(new ApiFieldError("fields", "Duplicate field: " + field.originalName()));
            }
        }
        return errors;
    }

    private String canonicalField(NormalizedField field) {
        return String.join(":",
                field.normalizedName(),
                field.type(),
                normalizeOptional(field.description()),
                normalizeOptional(field.declaredCategory()),
                sha256(normalizeOptional(field.sampleHint())));
    }

    private List<String> normalizeList(List<String> values) {
        return values.stream()
                .map(DatasetNormalizer::normalizeToken)
                .filter(value -> !value.isBlank())
                .distinct()
                .sorted()
                .toList();
    }

    private String normalizeJurisdiction(String value) {
        String token = normalizeToken(value);
        return switch (token) {
            case "eu", "europe", "european_union" -> "EU";
            case "us", "usa", "united_states", "united_states_of_america" -> "US";
            case "ca", "california", "us_ca", "us_california" -> "US-CA";
            case "uk", "united_kingdom" -> "UK";
            default -> token.toUpperCase(Locale.ROOT);
        };
    }

    private String normalizeOptional(String value) {
        return value == null ? "" : value.trim();
    }

    private String normalizeText(String value) {
        return value == null ? "" : value.trim();
    }

    private String sha256(String value) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                    .digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is unavailable", exception);
        }
    }
}
