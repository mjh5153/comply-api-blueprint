package com.init_spring_bean_mvn.demo.dataset.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DetectionMethod {
    DETERMINISTIC_RULE("deterministic_rule"),
    DECLARED_METADATA("declared_metadata"),
    SAMPLE_HINT("sample_hint"),
    UNRESOLVED("unresolved");

    private final String value;

    DetectionMethod(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }

    public int priority() {
        return switch (this) {
            case DECLARED_METADATA -> 4;
            case DETERMINISTIC_RULE -> 3;
            case SAMPLE_HINT -> 2;
            case UNRESOLVED -> 1;
        };
    }

    public static DetectionMethod fromSource(String source) {
        return switch (source) {
            case "declared_category" -> DECLARED_METADATA;
            case "sample_shape" -> SAMPLE_HINT;
            default -> DETERMINISTIC_RULE;
        };
    }
}
