package com.comply.ai.agents;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Represents the result of executing an action.
 */
public class ActionResult {
    private final boolean success;
    private final Object result;
    private final String errorMessage;
    private final long executionTimeMs;
    private final Map<String, Object> metadata;
    private final LocalDateTime executedAt;
    
    private ActionResult(boolean success, Object result, String errorMessage, 
                        long executionTimeMs, Map<String, Object> metadata) {
        this.success = success;
        this.result = result;
        this.errorMessage = errorMessage;
        this.executionTimeMs = executionTimeMs;
        this.metadata = metadata;
        this.executedAt = LocalDateTime.now();
    }
    
    public static ActionResult success(Object result, long executionTimeMs, Map<String, Object> metadata) {
        return new ActionResult(true, result, null, executionTimeMs, metadata);
    }
    
    public static ActionResult failure(String errorMessage, long executionTimeMs) {
        return new ActionResult(false, null, errorMessage, executionTimeMs, null);
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public Object getResult() {
        return result;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public long getExecutionTimeMs() {
        return executionTimeMs;
    }
    
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    public LocalDateTime getExecutedAt() {
        return executedAt;
    }
    
    @Override
    public String toString() {
        return "ActionResult{" +
                "success=" + success +
                ", result=" + (result != null ? result.toString() : "null") +
                ", errorMessage='" + errorMessage + '\'' +
                ", executionTimeMs=" + executionTimeMs +
                '}';
    }
}