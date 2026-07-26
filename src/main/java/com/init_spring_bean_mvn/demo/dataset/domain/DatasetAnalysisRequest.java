package com.init_spring_bean_mvn.demo.dataset.domain;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record DatasetAnalysisRequest(
        @NotBlank @Size(max = 200) String datasetName,
        @NotEmpty @Size(max = 20) List<@NotBlank @Size(max = 80) String> jurisdictions,
        @NotBlank @Size(max = 80) String businessRole,
        @NotEmpty @Size(max = 30) List<@NotBlank @Size(max = 100) String> processingPurposes,
        @NotEmpty @Size(max = 30) List<@NotBlank @Size(max = 100) String> processingActivities,
        @Min(0) @Max(36500) Integer retentionDays,
        Boolean thirdPartySharing,
        Boolean internationalTransfer,
        Boolean automatedDecisionMaking,
        Boolean usedForTraining,
        Boolean usedForInference,
        @NotEmpty @Size(max = 500) List<@Valid DatasetFieldRequest> fields
) {

    @Override
    public String toString() {
        long sampleHintCount = fields == null ? 0 : fields.stream()
                .filter(field -> field != null && field.sampleHint() != null && !field.sampleHint().isBlank())
                .count();
        return "DatasetAnalysisRequest[field_count=" + (fields == null ? 0 : fields.size())
                + ", sample_hint_count=" + sampleHintCount + "]";
    }
}
