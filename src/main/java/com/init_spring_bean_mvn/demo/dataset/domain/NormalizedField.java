package com.init_spring_bean_mvn.demo.dataset.domain;

public record NormalizedField(
        String originalName,
        String normalizedName,
        String type,
        String description,
        String declaredCategory,
        String sampleHint
) {
}
