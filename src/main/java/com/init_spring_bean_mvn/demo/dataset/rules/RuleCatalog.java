package com.init_spring_bean_mvn.demo.dataset.rules;

import java.util.List;

public record RuleCatalog(
        String version,
        List<RuleDefinition> rules
) {
    public RuleCatalog {
        rules = List.copyOf(rules);
    }
}
