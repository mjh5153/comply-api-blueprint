package com.init_spring_bean_mvn.demo.dataset.rules;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class YamlRuleCatalogLoader {

    private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory())
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

    private RuleCatalog catalog;

    @PostConstruct
    public void load() {
        try {
            Resource[] resources = new PathMatchingResourcePatternResolver()
                    .getResources("classpath*:rules/*.yml");
            if (resources.length == 0) {
                throw new IllegalStateException("No dataset analysis rule resources found");
            }

            List<RuleBundle> bundles = java.util.Arrays.stream(resources)
                    .map(this::readBundle)
                    .toList();
            String version = bundles.get(0).version();
            if (version == null || version.isBlank() || bundles.stream().anyMatch(bundle -> !version.equals(bundle.version()))) {
                throw new IllegalStateException("Rule resources must use one non-empty rule-set version");
            }

            List<RuleDefinition> rules = bundles.stream()
                    .flatMap(bundle -> bundle.rules().stream())
                    .toList();
            validateRules(rules);
            this.catalog = new RuleCatalog(version, rules);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load dataset analysis rules", exception);
        }
    }

    public RuleCatalog catalog() {
        if (catalog == null) {
            load();
        }
        return catalog;
    }

    private RuleBundle readBundle(Resource resource) {
        try (InputStream stream = resource.getInputStream()) {
            RuleBundle bundle = mapper.readValue(stream, RuleBundle.class);
            if (bundle == null || bundle.rules() == null) {
                throw new IllegalStateException("Rule resource is empty: " + resource.getFilename());
            }
            return bundle;
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to parse rule resource: " + resource.getFilename(), exception);
        }
    }

    private void validateRules(List<RuleDefinition> rules) {
        Set<String> ids = new HashSet<>();
        for (RuleDefinition rule : rules) {
            if (rule.id() == null || rule.id().isBlank() || !ids.add(rule.id())) {
                throw new IllegalStateException("Rule IDs must be non-empty and unique: " + rule.id());
            }
            if (isBlank(rule.version()) || isBlank(rule.framework()) || isBlank(rule.frameworkVersion())
                    || isBlank(rule.frameworkType()) || isBlank(rule.status()) || rule.effectiveFrom() == null
                    || rule.when() == null || rule.then() == null || isBlank(rule.rationale()) || rule.source() == null
                    || isBlank(rule.source().authority()) || isBlank(rule.source().citation())) {
                throw new IllegalStateException("Rule is missing required metadata: " + rule.id());
            }
            if (rule.jurisdictions() == null || rule.jurisdictions().isEmpty()
                    || rule.then().references() == null || rule.then().references().isEmpty()) {
                throw new IllegalStateException("Rule requires jurisdictions and references: " + rule.id());
            }
            if (isBlank(rule.then().applicability()) || isBlank(rule.then().risk())) {
                throw new IllegalStateException("Rule requires applicability and risk: " + rule.id());
            }
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
