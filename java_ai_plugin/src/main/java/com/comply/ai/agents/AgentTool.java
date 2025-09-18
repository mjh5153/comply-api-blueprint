package com.comply.ai.agents;

import com.comply.ai.core.Plugin;
import com.comply.ai.core.PluginException;

/**
 * Interface for agent tools that can be used during plan execution.
 */
public interface AgentTool extends Plugin {
    
    /**
     * Execute the tool with given parameters.
     * 
     * @param parameters Tool execution parameters
     * @return Result of tool execution
     * @throws PluginException if tool execution fails
     */
    ActionResult execute(java.util.Map<String, Object> parameters) throws PluginException;
    
    /**
     * Get the tool's capabilities and parameter schema.
     * 
     * @return Tool capabilities description
     */
    ToolCapabilities getCapabilities();
    
    /**
     * Validate that the provided parameters are valid for this tool.
     * 
     * @param parameters Parameters to validate
     * @return true if parameters are valid
     */
    boolean validateParameters(java.util.Map<String, Object> parameters);
}