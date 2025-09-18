package com.comply.ai.agents;

import com.comply.ai.core.Plugin;
import com.comply.ai.core.PluginException;

/**
 * Interface for planning modules that create execution plans for agent tasks.
 */
public interface PlanningModule extends Plugin {
    
    /**
     * Create a plan based on the given context and available tools.
     * 
     * @param context The current agent context
     * @param availableTools List of available tools for execution
     * @return Generated plan
     * @throws PluginException if planning fails
     */
    AgentPlan createPlan(AgentContext context, java.util.List<AgentTool> availableTools) throws PluginException;
    
    /**
     * Adapt an existing plan based on execution results.
     * 
     * @param originalPlan The original plan
     * @param executionResults Results from executed actions
     * @param context Current context
     * @return Adapted plan or null if no adaptation needed
     * @throws PluginException if adaptation fails
     */
    AgentPlan adaptPlan(AgentPlan originalPlan, java.util.List<ActionResult> executionResults, 
                       AgentContext context) throws PluginException;
    
    /**
     * Validate that a plan is executable with available tools.
     * 
     * @param plan Plan to validate
     * @param availableTools Available tools
     * @return true if plan is valid
     */
    boolean validatePlan(AgentPlan plan, java.util.List<AgentTool> availableTools);
}