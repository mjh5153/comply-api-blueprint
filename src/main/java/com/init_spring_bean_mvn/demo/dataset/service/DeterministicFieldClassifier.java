package com.init_spring_bean_mvn.demo.dataset.service;

import com.init_spring_bean_mvn.demo.dataset.domain.ClassificationEvidence;
import com.init_spring_bean_mvn.demo.dataset.domain.ClassificationResult;
import com.init_spring_bean_mvn.demo.dataset.domain.ClassificationSummary;
import com.init_spring_bean_mvn.demo.dataset.domain.DataCategory;
import com.init_spring_bean_mvn.demo.dataset.domain.DetectionMethod;
import com.init_spring_bean_mvn.demo.dataset.domain.NormalizedField;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

@Component
public class DeterministicFieldClassifier {

    private static final Pattern EMAIL = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private static final Pattern IPV4 = Pattern.compile("^(?:\\d{1,3}\\.){3}\\d{1,3}$");
    private static final Pattern SSN = Pattern.compile("^\\d{3}-\\d{2}-\\d{4}$");
    private static final Pattern PHONE = Pattern.compile("^\\+?[0-9() .-]{7,}$");

    public ClassificationSummary classify(List<NormalizedField> fields) {
        Map<DataCategory, CategoryAccumulator> categories = new EnumMap<>(DataCategory.class);
        Map<String, Set<DataCategory>> fieldCategories = new TreeMap<>();
        List<String> unresolvedFields = new ArrayList<>();

        for (NormalizedField field : fields) {
            List<Signal> signals = signalsFor(field);
            if (signals.isEmpty()) {
                signals = List.of(new Signal(
                        DataCategory.UNRESOLVED,
                        0.20,
                        DetectionMethod.UNRESOLVED,
                        "No deterministic signal matched the field name, metadata, type, or sample shape",
                        "unresolved"));
                unresolvedFields.add(field.originalName());
            }

            Set<DataCategory> fieldCategorySet = EnumSet.noneOf(DataCategory.class);
            for (Signal signal : signals) {
                fieldCategorySet.add(signal.category());
                categories.computeIfAbsent(signal.category(), ignored -> new CategoryAccumulator())
                        .add(field.originalName(), signal);
            }
            fieldCategories.put(field.originalName(), fieldCategorySet);
        }

        List<ClassificationResult> results = categories.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(DataCategory::value)))
                .map(entry -> entry.getValue().toResult(entry.getKey()))
                .toList();

        return new ClassificationSummary(
                results,
                fieldCategories,
                categories.keySet(),
                unresolvedFields.stream().sorted().toList());
    }

    private List<Signal> signalsFor(NormalizedField field) {
        List<Signal> signals = new ArrayList<>();
        String name = field.normalizedName();

        DataCategory declared = DataCategory.fromValue(field.declaredCategory());
        if (declared != null && declared != DataCategory.UNRESOLVED) {
            signals.add(new Signal(declared, 0.95, DetectionMethod.DECLARED_METADATA,
                    "Declared semantic category", "declared_category"));
        }

        addNameSignals(name, signals);
        addTypeSignals(field.type(), signals);
        addSampleSignals(field, signals);

        Map<DataCategory, Signal> best = new EnumMap<>(DataCategory.class);
        for (Signal signal : signals) {
            Signal previous = best.get(signal.category());
            if (previous == null || signal.method().priority() > previous.method().priority()
                    || (signal.method().priority() == previous.method().priority()
                    && signal.confidence() > previous.confidence())) {
                best.put(signal.category(), signal);
            }
        }
        return best.values().stream()
                .sorted(Comparator.comparing(signal -> signal.category().value()))
                .toList();
    }

    private void addNameSignals(String name, List<Signal> signals) {
        if (matches(name, "email", "email_address", "e_mail")) {
            add(signals, DataCategory.CONTACT_INFORMATION, "Field name matches email alias", "field_name", 0.99);
        }
        if (matches(name, "phone", "telephone", "mobile", "cell_phone")) {
            add(signals, DataCategory.TELEPHONE_NUMBER, "Field name matches telephone alias", "field_name", 0.99);
        }
        if (matches(name, "name", "full_name", "first_name", "last_name", "person_name", "cardholder_name")) {
            add(signals, DataCategory.PERSON_NAME, "Field name matches person-name alias", "field_name", 0.96);
        }
        boolean ipField = matches(name, "ip", "ip_address", "ipv4", "ipv6");
        if (!ipField && matches(name, "address", "postal_address", "street", "street_address", "zip", "postal_code")) {
            add(signals, DataCategory.POSTAL_ADDRESS, "Field name matches postal-address alias", "field_name", 0.96);
        }
        if (ipField) {
            add(signals, DataCategory.ONLINE_IDENTIFIER, "Field name matches IP-address alias", "field_name", 0.99);
        }
        if (matches(name, "device_id", "advertising_id", "cookie_id", "user_agent", "browser_id")) {
            add(signals, DataCategory.DEVICE_IDENTIFIER, "Field name matches device-identifier alias", "field_name", 0.97);
        }
        if (matches(name, "latitude", "longitude", "lat", "lon", "location", "geolocation", "gps")) {
            add(signals, DataCategory.GEOLOCATION, "Field name matches location alias", "field_name", 0.96);
        }
        if (matches(name, "dob", "date_of_birth", "birth_date", "birthdate")) {
            add(signals, DataCategory.DATE_OF_BIRTH, "Field name matches date-of-birth alias", "field_name", 0.99);
        }
        if (matches(name, "ssn", "social_security_number", "social_security")) {
            add(signals, DataCategory.SOCIAL_SECURITY_NUMBER, "Field name matches Social Security alias", "field_name", 0.99);
        }
        if (matches(name, "passport", "passport_number", "drivers_license", "driver_license", "national_id", "tax_id", "government_id")) {
            add(signals, DataCategory.GOVERNMENT_IDENTIFIER, "Field name matches government-identifier alias", "field_name", 0.98);
        }
        if (matches(name, "account_id", "account_number", "customer_id", "user_id", "member_id", "client_id")) {
            add(signals, DataCategory.ACCOUNT_IDENTIFIER, "Field name matches account-identifier alias", "field_name", 0.95);
        }
        if (matches(name, "card_number", "card_no", "pan", "cvv", "cvc", "cardholder_name", "payment_card")) {
            add(signals, DataCategory.PAYMENT_CARD_DATA, "Field name matches payment-card alias", "field_name", 0.99);
        }
        if (matches(name, "amount", "transaction_amount", "price", "cost", "balance", "payment_amount", "transaction_id", "purchase")) {
            add(signals, DataCategory.FINANCIAL_DATA, "Field name matches financial-transaction alias", "field_name", 0.90);
        }
        if (matches(name, "patient_id", "diagnosis", "diagnosis_code", "treatment", "treatment_notes", "medical", "health", "symptom", "medication")) {
            add(signals, DataCategory.HEALTH_DATA, "Field name matches health-data alias", "field_name", 0.98);
        }
        if (matches(name, "biometric", "fingerprint", "face_id", "facial_template", "iris", "voiceprint")) {
            add(signals, DataCategory.BIOMETRIC_DATA, "Field name matches biometric alias", "field_name", 0.98);
        }
        if (matches(name, "employee_id", "employee", "salary", "job_title", "department", "performance", "hire_date")) {
            add(signals, DataCategory.EMPLOYMENT_DATA, "Field name matches employment-data alias", "field_name", 0.94);
        }
        if (matches(name, "password", "password_hash", "secret", "token", "api_key", "credential", "auth_token")) {
            add(signals, DataCategory.AUTHENTICATION_CREDENTIALS, "Field name matches authentication alias", "field_name", 0.99);
        }
        if (matches(name, "click", "clickstream", "browsing", "behavior", "behaviour", "preference", "feedback", "activity")) {
            add(signals, DataCategory.BEHAVIORAL_DATA, "Field name matches behavioral-data alias", "field_name", 0.90);
        }
        if (matches(name, "notes", "comments", "comment", "description", "free_text", "text", "message", "prompt", "response")) {
            add(signals, DataCategory.FREE_FORM_TEXT, "Field name matches free-form-text alias", "field_name", 0.92);
        }
        if (matches(name, "child", "children", "minor", "child_id", "minor_id")) {
            add(signals, DataCategory.CHILDRENS_DATA, "Field name matches children-data indicator", "field_name", 0.85);
        }
    }

    private void addTypeSignals(String type, List<Signal> signals) {
        if (type.contains("email")) {
            add(signals, DataCategory.CONTACT_INFORMATION, "Declared type is email-like", "declared_type", 0.97);
        } else if (type.contains("phone") || type.contains("telephone")) {
            add(signals, DataCategory.TELEPHONE_NUMBER, "Declared type is telephone-like", "declared_type", 0.97);
        } else if (type.contains("ip")) {
            add(signals, DataCategory.ONLINE_IDENTIFIER, "Declared type is IP-like", "declared_type", 0.97);
        }
    }

    private void addSampleSignals(NormalizedField field, List<Signal> signals) {
        String sample = field.sampleHint();
        if (sample == null || sample.isBlank()) {
            return;
        }
        String trimmed = sample.trim();
        if (EMAIL.matcher(trimmed).matches()) {
            add(signals, DataCategory.CONTACT_INFORMATION, "Sample has email shape", "sample_shape", 0.96);
        }
        if (IPV4.matcher(trimmed).matches()) {
            add(signals, DataCategory.ONLINE_IDENTIFIER, "Sample has IPv4 shape", "sample_shape", 0.96);
        }
        if (SSN.matcher(trimmed).matches()) {
            add(signals, DataCategory.SOCIAL_SECURITY_NUMBER, "Sample has Social Security number shape", "sample_shape", 0.96);
        }
        if (!IPV4.matcher(trimmed).matches()
                && PHONE.matcher(trimmed).matches()
                && trimmed.replaceAll("\\D", "").length() >= 7) {
            add(signals, DataCategory.TELEPHONE_NUMBER, "Sample has telephone shape", "sample_shape", 0.90);
        }
        if (field.normalizedName().contains("card") && trimmed.replaceAll("\\D", "").length() >= 13) {
            add(signals, DataCategory.PAYMENT_CARD_DATA, "Sample has payment-card number shape", "sample_shape", 0.95);
        }
    }

    private boolean matches(String fieldName, String... aliases) {
        for (String alias : aliases) {
            String normalizedAlias = DatasetNormalizer.normalizeToken(alias);
            if (fieldName.equals(normalizedAlias)
                    || fieldName.startsWith(normalizedAlias + "_")
                    || fieldName.endsWith("_" + normalizedAlias)
                    || fieldName.contains("_" + normalizedAlias + "_")) {
                return true;
            }
        }
        return false;
    }

    private void add(List<Signal> signals, DataCategory category, String signal, String source, double confidence) {
        signals.add(new Signal(category, confidence, DetectionMethod.fromSource(source), signal, source));
    }

    private record Signal(
            DataCategory category,
            double confidence,
            DetectionMethod method,
            String signal,
            String source
    ) {
    }

    private static final class CategoryAccumulator {
        private final Set<String> fields = new LinkedHashSet<>();
        private final List<ClassificationEvidence> evidence = new ArrayList<>();
        private double confidence;
        private DetectionMethod method = DetectionMethod.UNRESOLVED;

        void add(String field, Signal signal) {
            fields.add(field);
            confidence = Math.max(confidence, signal.confidence());
            if (signal.method().priority() > method.priority()) {
                method = signal.method();
            }
            evidence.add(new ClassificationEvidence(field, signal.signal(), signal.source()));
        }

        ClassificationResult toResult(DataCategory category) {
            return new ClassificationResult(
                    category,
                    fields.stream().sorted().toList(),
                    round(confidence),
                    method,
                    evidence.stream()
                            .sorted(Comparator.comparing(ClassificationEvidence::field)
                                    .thenComparing(ClassificationEvidence::signal))
                            .toList());
        }

        private double round(double value) {
            return Math.round(value * 100.0) / 100.0;
        }
    }
}
