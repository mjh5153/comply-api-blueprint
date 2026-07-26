package com.init_spring_bean_mvn.demo.dataset;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DatasetAnalysisControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void euCustomerDatasetReturnsGdprMappingAndTraceableControls() throws Exception {
        performAnalysis("""
                {
                  "dataset_name": "CustomerTransactions",
                  "jurisdictions": ["EU"],
                  "business_role": "controller",
                  "processing_purposes": ["analytics", "marketing"],
                  "processing_activities": ["collection", "storage", "analytics"],
                  "retention_days": 365,
                  "fields": [
                    {"name": "email", "type": "string", "sample_hint": "person@example.com"},
                    {"name": "phone", "type": "string"},
                    {"name": "ip_address", "type": "string", "sample_hint": "192.0.2.10"}
                  ]
                }
                """)
                .andExpect(jsonPath("$.detected_data_categories[*].category", hasItems(
                        "contact_information", "telephone_number", "online_identifier")))
                .andExpect(jsonPath("$.applicable_frameworks[*].framework", hasItem("GDPR")))
                .andExpect(jsonPath("$.recommended_controls[*].control", hasItem("document_lawful_basis")))
                .andExpect(jsonPath("$.warnings", hasItem("This result is compliance-analysis support and not legal advice.")));
    }

    @Test
    void healthcareDatasetReturnsCautiousHipaaIndicator() throws Exception {
        performAnalysis("""
                {
                  "dataset_name": "PatientCare",
                  "jurisdictions": ["US"],
                  "business_role": "controller",
                  "processing_purposes": ["healthcare_operations"],
                  "processing_activities": ["collection", "storage"],
                  "fields": [
                    {"name": "patient_id", "type": "string"},
                    {"name": "diagnosis_code", "type": "string"},
                    {"name": "treatment_notes", "type": "string"}
                  ]
                }
                """)
                .andExpect(jsonPath("$.detected_data_categories[*].category", hasItem("health_data")))
                .andExpect(jsonPath("$.applicable_frameworks[*].framework", hasItem("HIPAA")))
                .andExpect(jsonPath("$.applicable_frameworks[?(@.framework == 'HIPAA')].applicability", hasItem("possible")))
                .andExpect(jsonPath("$.applicable_frameworks[?(@.framework == 'HIPAA')].missing_information[*]", hasItem("Confirm whether the organization is a covered entity or business associate.")));
    }

    @Test
    void paymentDatasetReturnsPciScopeIndicator() throws Exception {
        performAnalysis("""
                {
                  "dataset_name": "Payments",
                  "jurisdictions": ["US"],
                  "business_role": "service_provider",
                  "processing_purposes": ["payment_processing"],
                  "processing_activities": ["collection", "storage", "transmission"],
                  "fields": [
                    {"name": "card_number", "type": "string"},
                    {"name": "expiration_date", "type": "string"},
                    {"name": "cardholder_name", "type": "string"},
                    {"name": "amount", "type": "decimal"}
                  ]
                }
                """)
                .andExpect(jsonPath("$.detected_data_categories[*].category", hasItems("payment_card_data", "financial_data")))
                .andExpect(jsonPath("$.applicable_frameworks[*].framework", hasItem("PCI DSS")))
                .andExpect(jsonPath("$.recommended_controls[*].control", hasItem("tokenize_payment_card_data")));
    }

    @Test
    void ambiguousDatasetDoesNotInventClassification() throws Exception {
        performAnalysis("""
                {
                  "dataset_name": "UnknownValues",
                  "jurisdictions": ["EU"],
                  "business_role": "controller",
                  "processing_purposes": ["analytics"],
                  "processing_activities": ["storage"],
                  "fields": [
                    {"name": "value", "type": "string"},
                    {"name": "reference", "type": "string"},
                    {"name": "notes", "type": "string"}
                  ]
                }
                """)
                .andExpect(jsonPath("$.detected_data_categories[*].category", hasItem("unresolved")))
                .andExpect(jsonPath("$.detected_data_categories[*].category", hasItem("free_form_text")))
                .andExpect(jsonPath("$.assumptions", hasItem("Unresolved fields require additional semantic metadata or analyst review.")))
                .andExpect(jsonPath("$.warnings", hasItem("One or more fields could not be classified deterministically; review unresolved fields before relying on the result.")));
    }

    @Test
    void aiTrainingDatasetReturnsGovernanceSignalsWithoutLegalOverclaim() throws Exception {
        performAnalysis("""
                {
                  "dataset_name": "ModelFeedback",
                  "jurisdictions": ["EU"],
                  "business_role": "controller",
                  "processing_purposes": ["model_training"],
                  "processing_activities": ["collection", "training", "inference"],
                  "used_for_training": true,
                  "fields": [
                    {"name": "user_prompt", "type": "string"},
                    {"name": "model_response", "type": "string"},
                    {"name": "user_id", "type": "string"},
                    {"name": "feedback_score", "type": "decimal"}
                  ]
                }
                """)
                .andExpect(jsonPath("$.applicable_frameworks[*].framework", hasItems("GDPR", "EU AI Act", "ISO/IEC 42001")))
                .andExpect(jsonPath("$.applicable_frameworks[?(@.framework == 'EU AI Act')].applicability", hasItem("possible")))
                .andExpect(jsonPath("$.warnings", hasItem("Framework mappings are indicators based on supplied metadata, not final legal determinations.")));
    }

    @Test
    void safeOperationalDatasetDoesNotCreateUnsupportedPrivacyMapping() throws Exception {
        performAnalysis("""
                {
                  "dataset_name": "WarehouseInventory",
                  "jurisdictions": ["US"],
                  "business_role": "controller",
                  "processing_purposes": ["inventory_management"],
                  "processing_activities": ["storage", "analytics"],
                  "fields": [
                    {"name": "product_sku", "type": "string"},
                    {"name": "warehouse_id", "type": "string"},
                    {"name": "inventory_count", "type": "integer"}
                  ]
                }
                """)
                .andExpect(jsonPath("$.overall_risk").value("low"))
                .andExpect(jsonPath("$.applicable_frameworks[*].framework", not(hasItems("GDPR", "CCPA/CPRA", "HIPAA", "PCI DSS"))));
    }

    @Test
    void malformedRequestReturnsStableValidationEnvelope() throws Exception {
        mockMvc.perform(post("/v1/datasets/analyze")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Correlation-ID", "validation-test")
                        .content("{\"dataset_name\":\"bad\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("X-Correlation-ID", "validation-test"))
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.correlation_id").value("validation-test"));
    }

    private org.springframework.test.web.servlet.ResultActions performAnalysis(String json) throws Exception {
        return mockMvc.perform(post("/v1/datasets/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Correlation-ID", "dataset-test")
                .content(json))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Correlation-ID", "dataset-test"))
                .andExpect(jsonPath("$.scan_id").isString())
                .andExpect(jsonPath("$.api_version").value("v1"))
                .andExpect(jsonPath("$.engine_version").value("0.1.0"))
                .andExpect(jsonPath("$.rule_set_version").value("2026.1"));
    }
}
