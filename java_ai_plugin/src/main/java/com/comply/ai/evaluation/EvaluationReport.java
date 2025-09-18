package com.comply.ai.evaluation;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Comprehensive evaluation report with insights and recommendations.
 */
public class EvaluationReport {
    private final AggregatedMetrics metrics;
    private final String summary;
    private final List<String> insights;
    private final List<String> recommendations;
    private final LocalDateTime generatedAt;
    
    public EvaluationReport(AggregatedMetrics metrics, String summary,
                           List<String> insights, List<String> recommendations) {
        this.metrics = metrics;
        this.summary = summary;
        this.insights = insights;
        this.recommendations = recommendations;
        this.generatedAt = LocalDateTime.now();
    }
    
    public AggregatedMetrics getMetrics() {
        return metrics;
    }
    
    public String getSummary() {
        return summary;
    }
    
    public List<String> getInsights() {
        return insights;
    }
    
    public List<String> getRecommendations() {
        return recommendations;
    }
    
    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }
}