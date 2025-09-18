package com.comply.ai.retrieval;

import com.comply.ai.core.Document;
import com.comply.ai.core.PluginException;
import com.comply.ai.core.RetrievalResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Hybrid retrieval strategy that combines multiple retrievers.
 * Implements score fusion for combining results from different retrieval methods.
 */
public class HybridRetriever implements Retriever {
    private static final Logger logger = LoggerFactory.getLogger(HybridRetriever.class);
    
    private static final String PLUGIN_ID = "hybrid-retriever";
    private static final String VERSION = "1.0.0";
    
    private List<Retriever> retrievers;
    private Map<String, Double> weights;
    private boolean isInitialized = false;
    
    public HybridRetriever(List<Retriever> retrievers, Map<String, Double> weights) {
        this.retrievers = retrievers;
        this.weights = weights != null ? weights : createDefaultWeights();
    }
    
    @Override
    public void initialize(Map<String, Object> config) throws PluginException {
        // Initialize all component retrievers
        for (Retriever retriever : retrievers) {
            if (!retriever.isReady()) {
                retriever.initialize(config);
            }
        }
        this.isInitialized = true;
        logger.info("Initialized HybridRetriever with {} retrievers", retrievers.size());
    }
    
    @Override
    public void index(List<Document> documents) throws PluginException {
        if (!isInitialized) {
            throw new PluginException("HybridRetriever not initialized");
        }
        
        // Index documents in all retrievers
        for (Retriever retriever : retrievers) {
            retriever.index(documents);
        }
        
        logger.info("Indexed {} documents across {} retrievers", documents.size(), retrievers.size());
    }
    
    @Override
    public List<RetrievalResult> retrieve(String query, int maxResults) throws PluginException {
        if (!isInitialized) {
            throw new PluginException("HybridRetriever not initialized");
        }
        
        Map<String, List<RetrievalResult>> allResults = new HashMap<>();
        
        // Get results from all retrievers
        for (Retriever retriever : retrievers) {
            try {
                List<RetrievalResult> results = retriever.retrieve(query, maxResults);
                allResults.put(retriever.getPluginId(), results);
            } catch (Exception e) {
                logger.warn("Failed to retrieve from {}: {}", retriever.getPluginId(), e.getMessage());
            }
        }
        
        // Combine and rank results using score fusion
        Map<String, Double> combinedScores = new HashMap<>();
        Map<String, Document> documentMap = new HashMap<>();
        
        for (Map.Entry<String, List<RetrievalResult>> entry : allResults.entrySet()) {
            String retrieverId = entry.getKey();
            List<RetrievalResult> results = entry.getValue();
            double weight = weights.getOrDefault(retrieverId, 1.0);
            
            for (int i = 0; i < results.size(); i++) {
                RetrievalResult result = results.get(i);
                String docId = result.getDocument().getId();
                
                // Combine scores using weighted rank fusion
                double rankScore = (double) (results.size() - i) / results.size();
                double normalizedScore = result.getScore() * rankScore * weight;
                
                combinedScores.merge(docId, normalizedScore, Double::sum);
                documentMap.put(docId, result.getDocument());
            }
        }
        
        // Sort by combined scores and return top results
        List<RetrievalResult> finalResults = combinedScores.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(maxResults)
                .map(entry -> new RetrievalResult(
                        documentMap.get(entry.getKey()),
                        entry.getValue(),
                        PLUGIN_ID
                ))
                .collect(Collectors.toList());
        
        logger.debug("Retrieved {} hybrid results for query: {}", finalResults.size(), query);
        return finalResults;
    }
    
    @Override
    public int getIndexedDocumentCount() {
        return retrievers.stream()
                .mapToInt(Retriever::getIndexedDocumentCount)
                .max()
                .orElse(0);
    }
    
    @Override
    public void clearIndex() {
        retrievers.forEach(Retriever::clearIndex);
        logger.info("Cleared all indexes");
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
        metadata.put("type", "hybrid");
        metadata.put("componentRetrievers", retrievers.stream()
                .map(Retriever::getPluginId)
                .collect(Collectors.toList()));
        metadata.put("weights", weights);
        metadata.put("indexedDocuments", getIndexedDocumentCount());
        return metadata;
    }
    
    @Override
    public boolean isReady() {
        return isInitialized && retrievers.stream().allMatch(Retriever::isReady);
    }
    
    @Override
    public void shutdown() {
        retrievers.forEach(Retriever::shutdown);
        isInitialized = false;
        logger.info("HybridRetriever shutdown complete");
    }
    
    private Map<String, Double> createDefaultWeights() {
        Map<String, Double> defaultWeights = new HashMap<>();
        defaultWeights.put("bm25-retriever", 0.7);
        defaultWeights.put("vector-retriever", 0.3);
        return defaultWeights;
    }
}