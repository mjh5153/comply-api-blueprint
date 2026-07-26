package com.init_spring_bean_mvn.demo.dataset.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Applicability {
    APPLICABLE("applicable"),
    LIKELY("likely"),
    POSSIBLE("possible"),
    NOT_DETERMINED("not_determined"),
    NOT_APPLICABLE("not_applicable");

    private final String value;

    Applicability(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }

    public int rank() {
        return switch (this) {
            case APPLICABLE -> 4;
            case LIKELY -> 3;
            case POSSIBLE -> 2;
            case NOT_DETERMINED -> 1;
            case NOT_APPLICABLE -> 0;
        };
    }

    public static Applicability highest(Applicability first, Applicability second) {
        return first.rank() >= second.rank() ? first : second;
    }
}
