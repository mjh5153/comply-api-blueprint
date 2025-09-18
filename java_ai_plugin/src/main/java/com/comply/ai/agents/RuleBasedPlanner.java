package com.comply.ai.agents;

import com.comply.ai.core.PluginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Simple rule-based planning module that creates plans based on query patterns.
 */
public class RuleBasedPlanner implements PlanningModule {
    private static final Logger logger = LoggerFactory.getLogger(RuleBasedPlanner.class);
    
    private static final String PLUGIN_ID = "rule-based-planner";
    private static final String VERSION = "1.0.0";
    
    private boolean isInitialized = false;
    
    @Override
    public void initialize(Map<String, Object> config) throws PluginException {
        this.isInitialized = true;
        logger.info("Initialized RuleBasedPlanner with config: {}", config);
    }
    
    @Override
    public AgentPlan createPlan(AgentContext context, List<AgentTool> availableTools) throws PluginException {
        if (!isInitialized) {
            throw new PluginException("RuleBasedPlanner not initialized");
        }
        
        String query = context.getQuery().toLowerCase();
        List<PlannedAction> actions = new ArrayList<>();
        String strategy = "default";
        double confidence = 0.8;
        
        // Rule-based planning logic
        if (query.contains("search") || query.contains("find") || query.contains("retrieve")) {
            strategy = "retrieval-focused";
            
            // Plan retrieval action
            Map<String, Object> retrievalParams = new HashMap<>();
            retrievalParams.put("query", context.getQuery());
            retrievalParams.put("maxResults", 10);
            
            actions.add(new PlannedAction(
                "retrieve",
                "retriever-tool",
                retrievalParams,
                1,
                "Query indicates information retrieval need"
            ));
            
            confidence = 0.9;
        }
        
        if (query.contains("calculate") || query.contains("compute") || query.contains("math")) {
            strategy = "calculation-focused";
            
            // Plan calculation action
            Map<String, Object> calcParams = new HashMap<>();
            calcParams.put("expression", extractMathExpression(query));
            
            actions.add(new PlannedAction(
                "calculate",
                "calculator-tool",
                calcParams,
                1,
                "Query requires mathematical computation"
            ));
            
            confidence = 0.85;
        }
        
        if (query.contains("analyze") || query.contains("summarize") || query.contains("explain")) {
            strategy = "analysis-focused";
            
            // First retrieve relevant information
            Map<String, Object> retrievalParams = new HashMap<>();
            retrievalParams.put("query", context.getQuery());
            retrievalParams.put("maxResults", 5);
            
            actions.add(new PlannedAction(
                "retrieve",
                "retriever-tool",
                retrievalParams,
                1,
                "Need to gather information for analysis"
            ));
            
            // Then analyze the results
            Map<String, Object> analysisParams = new HashMap<>();
            analysisParams.put("task", "analyze");
            analysisParams.put("context", context.getQuery());
            
            actions.add(new PlannedAction(
                "analyze",
                "analysis-tool",
                analysisParams,
                2,
                "Perform analysis on retrieved information"
            ));
            
            confidence = 0.75;
        }
        
        // If no specific pattern matched, create a default retrieval plan
        if (actions.isEmpty()) {
            Map<String, Object> defaultParams = new HashMap<>();
            defaultParams.put("query", context.getQuery());
            defaultParams.put("maxResults", 5);
            
            actions.add(new PlannedAction(
                "retrieve",
                "retriever-tool",
                defaultParams,
                1,
                "Default action: attempt information retrieval"
            ));
            
            confidence = 0.6;
        }
        
        String planId = "plan_" + UUID.randomUUID().toString().substring(0, 8);
        AgentPlan plan = new AgentPlan(planId, actions, strategy, confidence);
        
        logger.debug("Created plan {} with {} actions, strategy: {}, confidence: {}", 
                    planId, actions.size(), strategy, confidence);
        
        return plan;
    }
    
    @Override
    public AgentPlan adaptPlan(AgentPlan originalPlan, List<ActionResult> executionResults, 
                              AgentContext context) throws PluginException {
        // Simple adaptation: if any action failed, try alternative approaches
        boolean hasFailures = executionResults.stream().anyMatch(r -> !r.isSuccess());
        
        if (!hasFailures) {
            return null; // No adaptation needed
        }
        
        logger.info("Adapting plan {} due to execution failures", originalPlan.getPlanId());
        
        // Create a simpler fallback plan
        List<PlannedAction> fallbackActions = new ArrayList<>();
        Map<String, Object> fallbackParams = new HashMap<>();
        fallbackParams.put("query", context.getQuery());
        fallbackParams.put("maxResults", 3);
        
        fallbackActions.add(new PlannedAction(
            "retrieve",
            "retriever-tool", 
            fallbackParams,
            1,
            "Fallback: simple retrieval after plan failure"
        ));
        
        String adaptedPlanId = originalPlan.getPlanId() + "_adapted";
        return new AgentPlan(adaptedPlanId, fallbackActions, "fallback", 0.5);
    }
    
    @Override
    public boolean validatePlan(AgentPlan plan, List<AgentTool> availableTools) {
        Set<String> availableToolNames = new HashSet<>();
        for (AgentTool tool : availableTools) {
            availableToolNames.add(tool.getPluginId());
        }
        
        // Check if all planned actions have corresponding tools
        for (PlannedAction action : plan.getActions()) {
            if (!availableToolNames.contains(action.getToolName())) {
                logger.warn("Plan validation failed: tool {} not available", action.getToolName());
                return false;
            }
        }
        
        return true;
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
        metadata.put("type", "rule-based");
        metadata.put("strategies", Arrays.asList("retrieval-focused", "calculation-focused", "analysis-focused", "default"));
        return metadata;
    }
    
    @Override
    public boolean isReady() {
        return isInitialized;
    }
    
    @Override
    public void shutdown() {
        isInitialized = false;
        logger.info("RuleBasedPlanner shutdown complete");
    }
    
    private String extractMathExpression(String query) {
        // Simple extraction - in practice, this would be more sophisticated
        String[] words = query.split("\\s+");
        StringBuilder expression = new StringBuilder();
        
        for (String word : words) {
            if (word.matches(".*\\d.*") || word.matches("[+\\-*/()=]")) {
                expression.append(word).append(" ");
            }
        }
        
        return expression.toString().trim();
    }
}