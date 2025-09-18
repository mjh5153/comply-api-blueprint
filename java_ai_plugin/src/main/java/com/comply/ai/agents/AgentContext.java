package com.comply.ai.agents;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Represents the context/environment that an agent perceives.
 */
public class AgentContext {
    private final String sessionId;
    private final String query;
    private final Map<String, Object> parameters;
    private final LocalDateTime timestamp;
    
    public AgentContext(String sessionId, String query, Map<String, Object> parameters) {
        this.sessionId = sessionId;
        this.query = query;
        this.parameters = parameters;
        this.timestamp = LocalDateTime.now();
    }
    
    public String getSessionId() {
        return sessionId;
    }
    
    public String getQuery() {
        return query;
    }
    
    public Map<String, Object> getParameters() {
        return parameters;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        return "AgentContext{" +
                "sessionId='" + sessionId + '\'' +
                ", query='" + query + '\'' +
                ", parameters=" + parameters +
                ", timestamp=" + timestamp +
                '}';
    }
}