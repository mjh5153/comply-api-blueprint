package com.comply.ai.agents;

import java.time.LocalDateTime;

/**
 * Represents a planned action that an agent intends to take.
 */
public class PlannedAction {
    private final String actionType;
    private final String toolName;
    private final java.util.Map<String, Object> parameters;
    private final int priority;
    private final String reasoning;
    private final LocalDateTime plannedAt;
    
    public PlannedAction(String actionType, String toolName, java.util.Map<String, Object> parameters, 
                        int priority, String reasoning) {
        this.actionType = actionType;
        this.toolName = toolName;
        this.parameters = parameters;
        this.priority = priority;
        this.reasoning = reasoning;
        this.plannedAt = LocalDateTime.now();
    }
    
    public String getActionType() {
        return actionType;
    }
    
    public String getToolName() {
        return toolName;
    }
    
    public java.util.Map<String, Object> getParameters() {
        return parameters;
    }
    
    public int getPriority() {
        return priority;
    }
    
    public String getReasoning() {
        return reasoning;
    }
    
    public LocalDateTime getPlannedAt() {
        return plannedAt;
    }
    
    @Override
    public String toString() {
        return "PlannedAction{" +
                "actionType='" + actionType + '\'' +
                ", toolName='" + toolName + '\'' +
                ", parameters=" + parameters +
                ", priority=" + priority +
                ", reasoning='" + reasoning + '\'' +
                '}';
    }
}