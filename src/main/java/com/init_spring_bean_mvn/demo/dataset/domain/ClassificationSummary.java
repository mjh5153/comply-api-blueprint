package com.init_spring_bean_mvn.demo.dataset.domain;

import java.util.List;
import java.util.Map;
import java.util.Set;

public record ClassificationSummary(
        List<ClassificationResult> results,
        Map<String, Set<DataCategory>> fieldCategories,
        Set<DataCategory> categories,
        List<String> unresolvedFields
) {
    public boolean has(DataCategory category) {
        return categories.contains(category);
    }

    public boolean hasSensitiveData() {
        return categories.stream().anyMatch(category -> switch (category) {
            case HEALTH_DATA, BIOMETRIC_DATA, PAYMENT_CARD_DATA, AUTHENTICATION_CREDENTIALS,
                 SOCIAL_SECURITY_NUMBER, GOVERNMENT_IDENTIFIER, CHILDRENS_DATA -> true;
            default -> false;
        });
    }

    public boolean hasDirectIdentifier() {
        return categories.stream().anyMatch(category -> switch (category) {
            case CONTACT_INFORMATION, TELEPHONE_NUMBER, PERSON_NAME, POSTAL_ADDRESS,
                 ONLINE_IDENTIFIER, DEVICE_IDENTIFIER, GEOLOCATION, DATE_OF_BIRTH,
                 GOVERNMENT_IDENTIFIER, SOCIAL_SECURITY_NUMBER, ACCOUNT_IDENTIFIER -> true;
            default -> false;
        });
    }
}
