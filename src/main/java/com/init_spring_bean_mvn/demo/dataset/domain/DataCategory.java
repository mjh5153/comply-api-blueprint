package com.init_spring_bean_mvn.demo.dataset.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

public enum DataCategory {
    CONTACT_INFORMATION("contact_information"),
    TELEPHONE_NUMBER("telephone_number"),
    PERSON_NAME("person_name"),
    POSTAL_ADDRESS("postal_address"),
    ONLINE_IDENTIFIER("online_identifier"),
    DEVICE_IDENTIFIER("device_identifier"),
    GEOLOCATION("geolocation"),
    DATE_OF_BIRTH("date_of_birth"),
    GOVERNMENT_IDENTIFIER("government_identifier"),
    SOCIAL_SECURITY_NUMBER("social_security_number"),
    ACCOUNT_IDENTIFIER("account_identifier"),
    PAYMENT_CARD_DATA("payment_card_data"),
    FINANCIAL_DATA("financial_data"),
    HEALTH_DATA("health_data"),
    BIOMETRIC_DATA("biometric_data"),
    EMPLOYMENT_DATA("employment_data"),
    AUTHENTICATION_CREDENTIALS("authentication_credentials"),
    BEHAVIORAL_DATA("behavioral_data"),
    FREE_FORM_TEXT("free_form_text"),
    CHILDRENS_DATA("childrens_data"),
    UNRESOLVED("unresolved");

    private final String value;

    DataCategory(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }

    @JsonCreator
    public static DataCategory fromValue(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim().toLowerCase(Locale.ROOT)
                .replace('-', '_')
                .replace(' ', '_');
        for (DataCategory category : values()) {
            if (category.value.equals(normalized) || category.name().equalsIgnoreCase(normalized)) {
                return category;
            }
        }
        return null;
    }
}
