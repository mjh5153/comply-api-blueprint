package com.comply.ai.core;

import java.util.Map;

/**
 * Base interface for all plugin components in the AI system.
 * Provides common functionality for initialization, configuration, and metadata.
 */
public interface Plugin {
    
    /**
     * Initialize the plugin with configuration parameters.
     * 
     * @param config Configuration parameters specific to this plugin
     * @throws PluginException if initialization fails
     */
    void initialize(Map<String, Object> config) throws PluginException;
    
    /**
     * Get the unique identifier for this plugin.
     * 
     * @return Plugin identifier
     */
    String getPluginId();
    
    /**
     * Get the version of this plugin.
     * 
     * @return Plugin version
     */
    String getVersion();
    
    /**
     * Get metadata about this plugin's capabilities.
     * 
     * @return Metadata map
     */
    Map<String, Object> getMetadata();
    
    /**
     * Check if the plugin is properly initialized and ready to use.
     * 
     * @return true if ready, false otherwise
     */
    boolean isReady();
    
    /**
     * Clean up resources and shutdown the plugin.
     */
    void shutdown();
}