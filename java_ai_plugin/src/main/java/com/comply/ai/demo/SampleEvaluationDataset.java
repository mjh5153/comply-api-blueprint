package com.comply.ai.demo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Sample dataset for evaluation with 50+ conversation turns.
 */
public class SampleEvaluationDataset {
    
    public static class ConversationTurn {
        @JsonProperty("turn_id")
        private String turnId;
        
        @JsonProperty("query")
        private String query;
        
        @JsonProperty("expected_intent")
        private String expectedIntent;
        
        @JsonProperty("expected_documents")
        private List<String> expectedDocuments;
        
        @JsonProperty("expected_calculation")
        private Double expectedCalculation;
        
        @JsonProperty("context")
        private Map<String, Object> context;
        
        @JsonProperty("timestamp")
        private LocalDateTime timestamp;
        
        // Constructors
        public ConversationTurn() {}
        
        public ConversationTurn(String turnId, String query, String expectedIntent, 
                               List<String> expectedDocuments, Double expectedCalculation,
                               Map<String, Object> context) {
            this.turnId = turnId;
            this.query = query;
            this.expectedIntent = expectedIntent;
            this.expectedDocuments = expectedDocuments;
            this.expectedCalculation = expectedCalculation;
            this.context = context;
            this.timestamp = LocalDateTime.now();
        }
        
        // Getters and setters
        public String getTurnId() { return turnId; }
        public void setTurnId(String turnId) { this.turnId = turnId; }
        
        public String getQuery() { return query; }
        public void setQuery(String query) { this.query = query; }
        
        public String getExpectedIntent() { return expectedIntent; }
        public void setExpectedIntent(String expectedIntent) { this.expectedIntent = expectedIntent; }
        
        public List<String> getExpectedDocuments() { return expectedDocuments; }
        public void setExpectedDocuments(List<String> expectedDocuments) { this.expectedDocuments = expectedDocuments; }
        
        public Double getExpectedCalculation() { return expectedCalculation; }
        public void setExpectedCalculation(Double expectedCalculation) { this.expectedCalculation = expectedCalculation; }
        
        public Map<String, Object> getContext() { return context; }
        public void setContext(Map<String, Object> context) { this.context = context; }
        
        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    }
    
    public static class EvaluationDataset {
        @JsonProperty("dataset_name")
        private String datasetName;
        
        @JsonProperty("version")
        private String version;
        
        @JsonProperty("description")
        private String description;
        
        @JsonProperty("conversation_turns")
        private List<ConversationTurn> conversationTurns;
        
        @JsonProperty("created_at")
        private LocalDateTime createdAt;
        
        // Constructors
        public EvaluationDataset() {}
        
        public EvaluationDataset(String datasetName, String version, String description, 
                                List<ConversationTurn> conversationTurns) {
            this.datasetName = datasetName;
            this.version = version;
            this.description = description;
            this.conversationTurns = conversationTurns;
            this.createdAt = LocalDateTime.now();
        }
        
        // Getters and setters
        public String getDatasetName() { return datasetName; }
        public void setDatasetName(String datasetName) { this.datasetName = datasetName; }
        
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public List<ConversationTurn> getConversationTurns() { return conversationTurns; }
        public void setConversationTurns(List<ConversationTurn> conversationTurns) { this.conversationTurns = conversationTurns; }
        
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    }
    
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    
    /**
     * Load the evaluation dataset from resources.
     */
    public static EvaluationDataset loadDataset() throws IOException {
        try (InputStream is = SampleEvaluationDataset.class.getResourceAsStream("/evaluation_dataset.json")) {
            if (is == null) {
                throw new IOException("Evaluation dataset not found in resources");
            }
            return objectMapper.readValue(is, EvaluationDataset.class);
        }
    }
}