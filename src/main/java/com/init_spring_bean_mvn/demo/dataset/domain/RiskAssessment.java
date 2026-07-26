package com.init_spring_bean_mvn.demo.dataset.domain;

import java.util.List;

public record RiskAssessment(
        RiskLevel level,
        List<RiskFactor> factors
) {
}
