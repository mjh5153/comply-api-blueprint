package com.comply.ai.evaluation;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Represents metrics collected during system execution.
 */
public class ExecutionMetrics {
    private final String sessionId;
    private final String planId;
    private final LocalDateTime timestamp;
    private final boolean planningSuccess;
    private final double planningTimeMs;
    private final double planConfidence;
    private final int totalActions;
    private final int successfulActions;
    private final int failedActions;
    private final double totalExecutionTimeMs;
    private final Map<String, Object> additionalMetrics;
    
    public ExecutionMetrics(String sessionId, String planId, boolean planningSuccess,
                           double planningTimeMs, double planConfidence, int totalActions,
                           int successfulActions, int failedActions, double totalExecutionTimeMs,
                           Map<String, Object> additionalMetrics) {
        this.sessionId = sessionId;
        this.planId = planId;
        this.timestamp = LocalDateTime.now();
        this.planningSuccess = planningSuccess;
        this.planningTimeMs = planningTimeMs;
        this.planConfidence = planConfidence;
        this.totalActions = totalActions;
        this.successfulActions = successfulActions;
        this.failedActions = failedActions;
        this.totalExecutionTimeMs = totalExecutionTimeMs;
        this.additionalMetrics = additionalMetrics;
    }
    
    public String getSessionId() {
        return sessionId;
    }
    
    public String getPlanId() {
        return planId;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public boolean isPlanningSuccess() {
        return planningSuccess;
    }
    
    public double getPlanningTimeMs() {
        return planningTimeMs;
    }
    
    public double getPlanConfidence() {
        return planConfidence;
    }
    
    public int getTotalActions() {
        return totalActions;
    }
    
    public int getSuccessfulActions() {
        return successfulActions;
    }
    
    public int getFailedActions() {
        return failedActions;
    }
    
    public double getTotalExecutionTimeMs() {
        return totalExecutionTimeMs;
    }
    
    public Map<String, Object> getAdditionalMetrics() {
        return additionalMetrics;
    }
    
    public double getSuccessRate() {
        return totalActions > 0 ? (double) successfulActions / totalActions : 0.0;
    }
    
    public double getAverageActionTime() {
        return totalActions > 0 ? totalExecutionTimeMs / totalActions : 0.0;
    }
    
    @Override
    public String toString() {
        return "ExecutionMetrics{" +
                "sessionId='" + sessionId + '\'' +
                ", planId='" + planId + '\'' +
                ", planningSuccess=" + planningSuccess +
                ", planConfidence=" + planConfidence +
                ", successRate=" + String.format("%.2f", getSuccessRate()) +
                ", totalExecutionTimeMs=" + totalExecutionTimeMs +
                '}';
    }
}