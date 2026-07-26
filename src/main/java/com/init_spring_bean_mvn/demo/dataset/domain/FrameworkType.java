package com.init_spring_bean_mvn.demo.dataset.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FrameworkType {
    LAW("law"),
    REGULATION("regulation"),
    INDUSTRY_STANDARD("industry_standard"),
    MANAGEMENT_SYSTEM_STANDARD("management_system_standard"),
    RISK_MANAGEMENT_GUIDANCE("risk_management_guidance");

    private final String value;

    FrameworkType(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }
}
