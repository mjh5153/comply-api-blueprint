package com.comply.ai.agents;

import com.comply.ai.core.Plugin;
import com.comply.ai.core.PluginException;

import java.util.List;

/**
 * Interface for execution modules that carry out planned actions.
 */
public interface ExecutionModule extends Plugin {
    
    /**
     * Execute a complete plan.
     * 
     * @param plan The plan to execute
     * @param tools Available tools for execution
     * @param context Execution context
     * @return Results from all executed actions
     * @throws PluginException if execution fails
     */
    List<ActionResult> executePlan(AgentPlan plan, List<AgentTool> tools, AgentContext context) 
            throws PluginException;
    
    /**
     * Execute a single action.
     * 
     * @param action The action to execute
     * @param tools Available tools
     * @param context Execution context
     * @return Result of action execution
     * @throws PluginException if execution fails
     */
    ActionResult executeAction(PlannedAction action, List<AgentTool> tools, AgentContext context) 
            throws PluginException;
    
    /**
     * Check if execution should continue based on current results.
     * 
     * @param results Current execution results
     * @param plan Original plan
     * @return true if execution should continue
     */
    boolean shouldContinue(List<ActionResult> results, AgentPlan plan);
}