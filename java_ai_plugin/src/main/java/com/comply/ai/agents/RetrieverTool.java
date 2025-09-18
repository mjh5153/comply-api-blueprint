package com.comply.ai.agents;

import com.comply.ai.core.Document;
import com.comply.ai.core.PluginException;
import com.comply.ai.core.RetrievalResult;
import com.comply.ai.retrieval.Retriever;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Agent tool that provides retrieval capabilities.
 */
public class RetrieverTool implements AgentTool {
    private static final Logger logger = LoggerFactory.getLogger(RetrieverTool.class);
    
    private static final String PLUGIN_ID = "retriever-tool";
    private static final String VERSION = "1.0.0";
    
    private Retriever retriever;
    private boolean isInitialized = false;
    
    public RetrieverTool(Retriever retriever) {
        this.retriever = retriever;
    }
    
    @Override
    public void initialize(Map<String, Object> config) throws PluginException {
        if (retriever != null && !retriever.isReady()) {
            retriever.initialize(config);
        }
        this.isInitialized = true;
        logger.info("Initialized RetrieverTool with retriever: {}", 
                   retriever != null ? retriever.getPluginId() : "null");
    }
    
    @Override
    public ActionResult execute(Map<String, Object> parameters) throws PluginException {
        if (!isInitialized) {
            throw new PluginException("RetrieverTool not initialized");
        }
        
        if (retriever == null) {
            return ActionResult.failure("No retriever configured", 0);
        }
        
        long startTime = System.currentTimeMillis();
        
        try {
            String query = (String) parameters.get("query");
            Integer maxResults = (Integer) parameters.getOrDefault("maxResults", 10);
            
            if (query == null || query.trim().isEmpty()) {
                return ActionResult.failure("Query parameter is required", 
                                          System.currentTimeMillis() - startTime);
            }
            
            List<RetrievalResult> results = retriever.retrieve(query, maxResults);
            
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("resultCount", results.size());
            metadata.put("retrieverType", retriever.getPluginId());
            metadata.put("query", query);
            
            return ActionResult.success(results, System.currentTimeMillis() - startTime, metadata);
            
        } catch (Exception e) {
            return ActionResult.failure("Retrieval failed: " + e.getMessage(),
                                      System.currentTimeMillis() - startTime);
        }
    }
    
    @Override
    public ToolCapabilities getCapabilities() {
        return new ToolCapabilities(
            "Retrieve relevant documents based on text queries",
            Arrays.asList("query"),
            Arrays.asList("maxResults"),
            Map.of(
                "query", "Text query to search for",
                "maxResults", "Maximum number of results to return (default: 10)"
            ),
            "List<RetrievalResult>"
        );
    }
    
    @Override
    public boolean validateParameters(Map<String, Object> parameters) {
        if (parameters == null) {
            return false;
        }
        
        String query = (String) parameters.get("query");
        if (query == null || query.trim().isEmpty()) {
            return false;
        }
        
        Object maxResults = parameters.get("maxResults");
        if (maxResults != null && !(maxResults instanceof Integer)) {
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
        metadata.put("type", "retrieval");
        metadata.put("retrieverPluginId", retriever != null ? retriever.getPluginId() : "none");
        return metadata;
    }
    
    @Override
    public boolean isReady() {
        return isInitialized && (retriever == null || retriever.isReady());
    }
    
    @Override
    public void shutdown() {
        isInitialized = false;
        logger.info("RetrieverTool shutdown complete");
    }
}