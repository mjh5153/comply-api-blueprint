package com.comply.ai.evaluation;

import java.util.Map;

/**
 * Aggregated metrics across multiple executions.
 */
public class AggregatedMetrics {
    private final int totalExecutions;
    private final int successfulPlanning;
    private final int failedPlanning;
    private final double averagePlanConfidence;
    private final double averagePlanningTime;
    private final double averageExecutionTime;
    private final double overallSuccessRate;
    private final Map<String, Integer> toolUsageStats;
    private final Map<String, Double> planningStrategyStats;
    
    public AggregatedMetrics(int totalExecutions, int successfulPlanning, int failedPlanning,
                            double averagePlanConfidence, double averagePlanningTime,
                            double averageExecutionTime, double overallSuccessRate,
                            Map<String, Integer> toolUsageStats,
                            Map<String, Double> planningStrategyStats) {
        this.totalExecutions = totalExecutions;
        this.successfulPlanning = successfulPlanning;
        this.failedPlanning = failedPlanning;
        this.averagePlanConfidence = averagePlanConfidence;
        this.averagePlanningTime = averagePlanningTime;
        this.averageExecutionTime = averageExecutionTime;
        this.overallSuccessRate = overallSuccessRate;
        this.toolUsageStats = toolUsageStats;
        this.planningStrategyStats = planningStrategyStats;
    }
    
    public int getTotalExecutions() {
        return totalExecutions;
    }
    
    public int getSuccessfulPlanning() {
        return successfulPlanning;
    }
    
    public int getFailedPlanning() {
        return failedPlanning;
    }
    
    public double getAveragePlanConfidence() {
        return averagePlanConfidence;
    }
    
    public double getAveragePlanningTime() {
        return averagePlanningTime;
    }
    
    public double getAverageExecutionTime() {
        return averageExecutionTime;
    }
    
    public double getOverallSuccessRate() {
        return overallSuccessRate;
    }
    
    public Map<String, Integer> getToolUsageStats() {
        return toolUsageStats;
    }
    
    public Map<String, Double> getPlanningStrategyStats() {
        return planningStrategyStats;
    }
    
    public double getPlanningSuccessRate() {
        return totalExecutions > 0 ? (double) successfulPlanning / totalExecutions : 0.0;
    }
    
    @Override
    public String toString() {
        return "AggregatedMetrics{" +
                "totalExecutions=" + totalExecutions +
                ", planningSuccessRate=" + String.format("%.2f", getPlanningSuccessRate()) +
                ", averagePlanConfidence=" + String.format("%.2f", averagePlanConfidence) +
                ", overallSuccessRate=" + String.format("%.2f", overallSuccessRate) +
                ", averageExecutionTime=" + String.format("%.1f", averageExecutionTime) + "ms" +
                '}';
    }
}