package com.comply.ai.evaluation;

import com.comply.ai.agents.ActionResult;
import com.comply.ai.agents.AgentContext;
import com.comply.ai.agents.AgentPlan;
import com.comply.ai.core.Plugin;
import com.comply.ai.core.PluginException;

import java.util.List;

/**
 * Interface for evaluation systems that collect and analyze execution metrics.
 */
public interface EvaluationFramework extends Plugin {
    
    /**
     * Record metrics from a completed execution session.
     * 
     * @param context The agent context
     * @param plan The executed plan
     * @param results Results from plan execution
     * @param planningTimeMs Time taken for planning
     * @param executionTimeMs Time taken for execution
     * @throws PluginException if recording fails
     */
    void recordExecution(AgentContext context, AgentPlan plan, List<ActionResult> results,
                        long planningTimeMs, long executionTimeMs) throws PluginException;
    
    /**
     * Get aggregated metrics for a time period.
     * 
     * @param fromTimestamp Start time (ISO format) or null for all time
     * @param toTimestamp End time (ISO format) or null for current time
     * @return Aggregated metrics
     * @throws PluginException if retrieval fails
     */
    AggregatedMetrics getAggregatedMetrics(String fromTimestamp, String toTimestamp) 
            throws PluginException;
    
    /**
     * Get detailed metrics for specific sessions.
     * 
     * @param sessionIds List of session IDs or null for all sessions
     * @param limit Maximum number of records to return
     * @return List of execution metrics
     * @throws PluginException if retrieval fails
     */
    List<ExecutionMetrics> getDetailedMetrics(List<String> sessionIds, int limit) 
            throws PluginException;
    
    /**
     * Generate an evaluation report based on collected metrics.
     * 
     * @param datasetSize Number of turns to include in report
     * @return Generated report
     * @throws PluginException if report generation fails
     */
    EvaluationReport generateReport(int datasetSize) throws PluginException;
}