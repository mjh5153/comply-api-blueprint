package com.comply.ai.agents;

import com.comply.ai.core.PluginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Sequential execution module that executes planned actions in order.
 */
public class SequentialExecutor implements ExecutionModule {
    private static final Logger logger = LoggerFactory.getLogger(SequentialExecutor.class);
    
    private static final String PLUGIN_ID = "sequential-executor";
    private static final String VERSION = "1.0.0";
    
    private boolean isInitialized = false;
    private boolean continueOnFailure = false;
    
    @Override
    public void initialize(Map<String, Object> config) throws PluginException {
        if (config != null) {
            this.continueOnFailure = (Boolean) config.getOrDefault("continueOnFailure", false);
        }
        this.isInitialized = true;
        logger.info("Initialized SequentialExecutor with continueOnFailure: {}", continueOnFailure);
    }
    
    @Override
    public List<ActionResult> executePlan(AgentPlan plan, List<AgentTool> tools, AgentContext context) 
            throws PluginException {
        if (!isInitialized) {
            throw new PluginException("SequentialExecutor not initialized");
        }
        
        logger.info("Executing plan {} with {} actions", plan.getPlanId(), plan.getActions().size());
        
        List<ActionResult> results = new ArrayList<>();
        
        // Sort actions by priority
        List<PlannedAction> sortedActions = new ArrayList<>(plan.getActions());
        sortedActions.sort(Comparator.comparingInt(PlannedAction::getPriority));
        
        for (PlannedAction action : sortedActions) {
            try {
                ActionResult result = executeAction(action, tools, context);
                results.add(result);
                
                logger.debug("Action {} completed: {}", action.getActionType(), 
                           result.isSuccess() ? "SUCCESS" : "FAILURE");
                
                // Check if we should continue execution
                if (!shouldContinue(results, plan)) {
                    logger.info("Stopping execution based on shouldContinue check");
                    break;
                }
                
            } catch (Exception e) {
                ActionResult errorResult = ActionResult.failure(
                    "Execution error: " + e.getMessage(), 0);
                results.add(errorResult);
                
                logger.error("Action {} failed with error: {}", action.getActionType(), e.getMessage());
                
                if (!continueOnFailure) {
                    logger.info("Stopping execution due to failure (continueOnFailure=false)");
                    break;
                }
            }
        }
        
        logger.info("Plan execution completed. {} actions executed, {} successful", 
                   results.size(), results.stream().mapToInt(r -> r.isSuccess() ? 1 : 0).sum());
        
        return results;
    }
    
    @Override
    public ActionResult executeAction(PlannedAction action, List<AgentTool> tools, AgentContext context) 
            throws PluginException {
        
        long startTime = System.currentTimeMillis();
        
        // Find the appropriate tool
        AgentTool tool = null;
        for (AgentTool t : tools) {
            if (t.getPluginId().equals(action.getToolName())) {
                tool = t;
                break;
            }
        }
        
        if (tool == null) {
            return ActionResult.failure(
                "Tool not found: " + action.getToolName(), 
                System.currentTimeMillis() - startTime);
        }
        
        // Validate parameters
        if (!tool.validateParameters(action.getParameters())) {
            return ActionResult.failure(
                "Invalid parameters for tool " + action.getToolName(),
                System.currentTimeMillis() - startTime);
        }
        
        try {
            // Execute the tool
            ActionResult result = tool.execute(action.getParameters());
            
            logger.debug("Tool {} executed in {}ms", 
                        action.getToolName(), System.currentTimeMillis() - startTime);
            
            return result;
            
        } catch (Exception e) {
            return ActionResult.failure(
                "Tool execution failed: " + e.getMessage(),
                System.currentTimeMillis() - startTime);
        }
    }
    
    @Override
    public boolean shouldContinue(List<ActionResult> results, AgentPlan plan) {
        if (results.isEmpty()) {
            return true;
        }
        
        // If we're not set to continue on failure, stop on first failure
        if (!continueOnFailure) {
            ActionResult lastResult = results.get(results.size() - 1);
            if (!lastResult.isSuccess()) {
                return false;
            }
        }
        
        // Check if we've executed all planned actions
        if (results.size() >= plan.getActions().size()) {
            return false;
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
        metadata.put("type", "sequential");
        metadata.put("continueOnFailure", continueOnFailure);
        return metadata;
    }
    
    @Override
    public boolean isReady() {
        return isInitialized;
    }
    
    @Override
    public void shutdown() {
        isInitialized = false;
        logger.info("SequentialExecutor shutdown complete");
    }
}