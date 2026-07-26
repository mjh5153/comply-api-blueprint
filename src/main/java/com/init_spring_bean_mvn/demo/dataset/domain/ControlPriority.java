package com.init_spring_bean_mvn.demo.dataset.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ControlPriority {
    LOW("low"),
    MEDIUM("medium"),
    HIGH("high"),
    CRITICAL("critical");

    private final String value;

    ControlPriority(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }

    public int rank() {
        return ordinal();
    }
}
