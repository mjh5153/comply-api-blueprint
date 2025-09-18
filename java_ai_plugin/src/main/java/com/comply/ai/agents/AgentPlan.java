package com.comply.ai.agents;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a complete plan consisting of multiple actions.
 */
public class AgentPlan {
    private final String planId;
    private final List<PlannedAction> actions;
    private final String strategy;
    private final double confidence;
    private final LocalDateTime createdAt;
    
    public AgentPlan(String planId, List<PlannedAction> actions, String strategy, double confidence) {
        this.planId = planId;
        this.actions = actions;
        this.strategy = strategy;
        this.confidence = confidence;
        this.createdAt = LocalDateTime.now();
    }
    
    public String getPlanId() {
        return planId;
    }
    
    public List<PlannedAction> getActions() {
        return actions;
    }
    
    public String getStrategy() {
        return strategy;
    }
    
    public double getConfidence() {
        return confidence;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    @Override
    public String toString() {
        return "AgentPlan{" +
                "planId='" + planId + '\'' +
                ", actions=" + actions.size() + " actions" +
                ", strategy='" + strategy + '\'' +
                ", confidence=" + confidence +
                '}';
    }
}