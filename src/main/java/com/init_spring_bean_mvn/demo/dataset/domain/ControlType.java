package com.init_spring_bean_mvn.demo.dataset.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ControlType {
    PREVENTIVE("preventive"),
    DETECTIVE("detective"),
    CORRECTIVE("corrective"),
    GOVERNANCE("governance"),
    DOCUMENTATION("documentation");

    private final String value;

    ControlType(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }
}
