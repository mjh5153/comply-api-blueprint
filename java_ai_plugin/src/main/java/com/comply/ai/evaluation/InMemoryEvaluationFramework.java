package com.comply.ai.evaluation;

import com.comply.ai.agents.ActionResult;
import com.comply.ai.agents.AgentContext;
import com.comply.ai.agents.AgentPlan;
import com.comply.ai.core.PluginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Simple in-memory evaluation framework for collecting and analyzing metrics.
 */
public class InMemoryEvaluationFramework implements EvaluationFramework {
    private static final Logger logger = LoggerFactory.getLogger(InMemoryEvaluationFramework.class);
    
    private static final String PLUGIN_ID = "inmemory-evaluation";
    private static final String VERSION = "1.0.0";
    
    private final List<ExecutionMetrics> allMetrics;
    private boolean isInitialized = false;
    
    public InMemoryEvaluationFramework() {
        this.allMetrics = new ArrayList<>();
    }
    
    @Override
    public void initialize(Map<String, Object> config) throws PluginException {
        this.isInitialized = true;
        logger.info("Initialized InMemoryEvaluationFramework");
    }
    
    @Override
    public void recordExecution(AgentContext context, AgentPlan plan, List<ActionResult> results,
                               long planningTimeMs, long executionTimeMs) throws PluginException {
        if (!isInitialized) {
            throw new PluginException("InMemoryEvaluationFramework not initialized");
        }
        
        int successfulActions = (int) results.stream().mapToInt(r -> r.isSuccess() ? 1 : 0).sum();
        int failedActions = results.size() - successfulActions;
        
        Map<String, Object> additionalMetrics = new HashMap<>();
        additionalMetrics.put("strategy", plan.getStrategy());
        additionalMetrics.put("query", context.getQuery());
        additionalMetrics.put("contextParameters", context.getParameters());
        
        ExecutionMetrics metrics = new ExecutionMetrics(
            context.getSessionId(),
            plan.getPlanId(),
            true, // Planning was successful if we got here
            planningTimeMs,
            plan.getConfidence(),
            results.size(),
            successfulActions,
            failedActions,
            executionTimeMs,
            additionalMetrics
        );
        
        synchronized (allMetrics) {
            allMetrics.add(metrics);
        }
        
        logger.debug("Recorded execution metrics for session {}, plan {}", 
                    context.getSessionId(), plan.getPlanId());
    }
    
    @Override
    public AggregatedMetrics getAggregatedMetrics(String fromTimestamp, String toTimestamp) 
            throws PluginException {
        
        List<ExecutionMetrics> filteredMetrics;
        synchronized (allMetrics) {
            filteredMetrics = new ArrayList<>(allMetrics);
        }
        
        // Filter by timestamp if provided
        if (fromTimestamp != null || toTimestamp != null) {
            LocalDateTime from = fromTimestamp != null ? 
                LocalDateTime.parse(fromTimestamp, DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
            LocalDateTime to = toTimestamp != null ? 
                LocalDateTime.parse(toTimestamp, DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
            
            filteredMetrics = filteredMetrics.stream()
                .filter(m -> (from == null || m.getTimestamp().isAfter(from)) && 
                           (to == null || m.getTimestamp().isBefore(to)))
                .collect(Collectors.toList());
        }
        
        if (filteredMetrics.isEmpty()) {
            return new AggregatedMetrics(0, 0, 0, 0.0, 0.0, 0.0, 0.0, 
                                       new HashMap<>(), new HashMap<>());
        }
        
        int totalExecutions = filteredMetrics.size();
        int successfulPlanning = (int) filteredMetrics.stream()
                                                     .mapToInt(m -> m.isPlanningSuccess() ? 1 : 0)
                                                     .sum();
        int failedPlanning = totalExecutions - successfulPlanning;
        
        double avgPlanConfidence = filteredMetrics.stream()
                                                 .mapToDouble(ExecutionMetrics::getPlanConfidence)
                                                 .average().orElse(0.0);
        
        double avgPlanningTime = filteredMetrics.stream()
                                               .mapToDouble(ExecutionMetrics::getPlanningTimeMs)
                                               .average().orElse(0.0);
        
        double avgExecutionTime = filteredMetrics.stream()
                                                .mapToDouble(ExecutionMetrics::getTotalExecutionTimeMs)
                                                .average().orElse(0.0);
        
        double overallSuccessRate = filteredMetrics.stream()
                                                  .mapToDouble(ExecutionMetrics::getSuccessRate)
                                                  .average().orElse(0.0);
        
        // Tool usage statistics
        Map<String, Integer> toolUsageStats = new HashMap<>();
        
        // Strategy statistics
        Map<String, Double> strategyStats = filteredMetrics.stream()
            .collect(Collectors.groupingBy(
                m -> (String) m.getAdditionalMetrics().get("strategy"),
                Collectors.averagingDouble(ExecutionMetrics::getSuccessRate)
            ));
        
        return new AggregatedMetrics(totalExecutions, successfulPlanning, failedPlanning,
                                   avgPlanConfidence, avgPlanningTime, avgExecutionTime,
                                   overallSuccessRate, toolUsageStats, strategyStats);
    }
    
    @Override
    public List<ExecutionMetrics> getDetailedMetrics(List<String> sessionIds, int limit) 
            throws PluginException {
        
        List<ExecutionMetrics> filteredMetrics;
        synchronized (allMetrics) {
            filteredMetrics = new ArrayList<>(allMetrics);
        }
        
        if (sessionIds != null && !sessionIds.isEmpty()) {
            Set<String> sessionIdSet = new HashSet<>(sessionIds);
            filteredMetrics = filteredMetrics.stream()
                .filter(m -> sessionIdSet.contains(m.getSessionId()))
                .collect(Collectors.toList());
        }
        
        // Sort by timestamp, most recent first
        filteredMetrics.sort((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));
        
        if (limit > 0 && filteredMetrics.size() > limit) {
            return filteredMetrics.subList(0, limit);
        }
        
        return filteredMetrics;
    }
    
    @Override
    public EvaluationReport generateReport(int datasetSize) throws PluginException {
        List<ExecutionMetrics> recentMetrics = getDetailedMetrics(null, datasetSize);
        
        if (recentMetrics.isEmpty()) {
            return new EvaluationReport(
                getAggregatedMetrics(null, null),
                "No execution data available",
                Arrays.asList("No insights available - no execution data"),
                Arrays.asList("Execute some agent sessions to generate meaningful reports")
            );
        }
        
        AggregatedMetrics aggregated = getAggregatedMetrics(null, null);
        
        // Generate insights
        List<String> insights = new ArrayList<>();
        if (aggregated.getOverallSuccessRate() > 0.8) {
            insights.add("High success rate indicates good system performance");
        } else if (aggregated.getOverallSuccessRate() < 0.5) {
            insights.add("Low success rate suggests system needs optimization");
        }
        
        if (aggregated.getAveragePlanConfidence() > 0.8) {
            insights.add("High planning confidence suggests good query understanding");
        }
        
        insights.add(String.format("Average execution time is %.1f ms per session", 
                                  aggregated.getAverageExecutionTime()));
        
        // Generate recommendations
        List<String> recommendations = new ArrayList<>();
        if (aggregated.getOverallSuccessRate() < 0.7) {
            recommendations.add("Consider improving tool reliability or adding fallback mechanisms");
        }
        
        if (aggregated.getAverageExecutionTime() > 5000) {
            recommendations.add("Execution times are high - consider optimizing tool performance");
        }
        
        if (aggregated.getAveragePlanConfidence() < 0.6) {
            recommendations.add("Low planning confidence - consider improving query understanding");
        }
        
        String summary = String.format(
            "System processed %d executions with %.1f%% success rate and %.1f%% planning success rate. " +
            "Average confidence: %.2f, Average execution time: %.1f ms",
            aggregated.getTotalExecutions(),
            aggregated.getOverallSuccessRate() * 100,
            aggregated.getPlanningSuccessRate() * 100,
            aggregated.getAveragePlanConfidence(),
            aggregated.getAverageExecutionTime()
        );
        
        return new EvaluationReport(aggregated, summary, insights, recommendations);
    }
    
    @Override
    public String getPluginId() {
        return PLUGIN_ID;
    }
    
    @Override
    public String getVersion() {
        return VERSION;
    }
    
    @Override
    public Map<String, Object> getMetadata() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("type", "in-memory");
        metadata.put("recordedExecutions", allMetrics.size());
        return metadata;
    }
    
    @Override
    public boolean isReady() {
        return isInitialized;
    }
    
    @Override
    public void shutdown() {
        synchronized (allMetrics) {
            allMetrics.clear();
        }
        isInitialized = false;
        logger.info("InMemoryEvaluationFramework shutdown complete");
    }
}