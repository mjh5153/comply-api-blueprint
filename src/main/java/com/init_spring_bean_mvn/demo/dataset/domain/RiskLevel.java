package com.init_spring_bean_mvn.demo.dataset.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RiskLevel {
    LOW("low"),
    MEDIUM("medium"),
    HIGH("high"),
    CRITICAL("critical");

    private final String value;

    RiskLevel(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }

    public int rank() {
        return ordinal();
    }

    public static RiskLevel highest(RiskLevel first, RiskLevel second) {
        return first.rank() >= second.rank() ? first : second;
    }
}
