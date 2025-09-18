package com.comply.ai.retrieval;

import com.comply.ai.core.Document;
import com.comply.ai.core.PluginException;
import com.comply.ai.core.RetrievalResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Simple vector-based retrieval using cosine similarity.
 * In a real implementation, this would use proper embedding models.
 */
public class VectorRetriever implements Retriever {
    private static final Logger logger = LoggerFactory.getLogger(VectorRetriever.class);
    
    private static final String PLUGIN_ID = "vector-retriever";
    private static final String VERSION = "1.0.0";
    
    private List<Document> indexedDocuments;
    private boolean isInitialized = false;
    
    @Override
    public void initialize(Map<String, Object> config) throws PluginException {
        this.indexedDocuments = new ArrayList<>();
        this.isInitialized = true;
        logger.info("Initialized VectorRetriever with config: {}", config);
    }
    
    @Override
    public void index(List<Document> documents) throws PluginException {
        if (!isInitialized) {
            throw new PluginException("VectorRetriever not initialized");
        }
        
        // Generate simple embeddings based on content (mock implementation)
        List<Document> documentsWithEmbeddings = documents.stream()
                .map(this::generateEmbedding)
                .collect(Collectors.toList());
        
        indexedDocuments.addAll(documentsWithEmbeddings);
        logger.info("Indexed {} documents with embeddings", documents.size());
    }
    
    @Override
    public List<RetrievalResult> retrieve(String query, int maxResults) throws PluginException {
        if (!isInitialized) {
            throw new PluginException("VectorRetriever not initialized");
        }
        
        // Generate query embedding
        List<Double> queryEmbedding = generateSimpleEmbedding(query);
        
        // Calculate similarities
        List<RetrievalResult> results = indexedDocuments.stream()
                .map(doc -> {
                    double similarity = cosineSimilarity(queryEmbedding, doc.getEmbedding());
                    return new RetrievalResult(doc, similarity, PLUGIN_ID);
                })
                .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
                .limit(maxResults)
                .collect(Collectors.toList());
        
        logger.debug("Retrieved {} results for query: {}", results.size(), query);
        return results;
    }
    
    @Override
    public int getIndexedDocumentCount() {
        return indexedDocuments.size();
    }
    
    @Override
    public void clearIndex() {
        indexedDocuments.clear();
        logger.info("Cleared index");
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
        metadata.put("type", "vector-based");
        metadata.put("algorithm", "cosine-similarity");
        metadata.put("embeddingDimension", 50); // Mock dimension
        metadata.put("indexedDocuments", getIndexedDocumentCount());
        return metadata;
    }
    
    @Override
    public boolean isReady() {
        return isInitialized;
    }
    
    @Override
    public void shutdown() {
        indexedDocuments = null;
        isInitialized = false;
        logger.info("VectorRetriever shutdown complete");
    }
    
    /**
     * Generate a simple embedding for a document (mock implementation).
     * In production, this would use a real embedding model.
     */
    private Document generateEmbedding(Document document) {
        if (document.getEmbedding() != null) {
            return document; // Already has embedding
        }
        
        List<Double> embedding = generateSimpleEmbedding(document.getContent());
        return document.withEmbedding(embedding);
    }
    
    /**
     * Generate a simple embedding based on character frequencies (mock implementation).
     */
    private List<Double> generateSimpleEmbedding(String text) {
        String lowerText = text.toLowerCase();
        List<Double> embedding = new ArrayList<>();
        
        // Simple frequency-based embedding (50 dimensions)
        for (int i = 0; i < 50; i++) {
            char c = (char) ('a' + (i % 26));
            long count = lowerText.chars().filter(ch -> ch == c).count();
            double frequency = (double) count / text.length();
            embedding.add(frequency);
        }
        
        return embedding;
    }
    
    /**
     * Calculate cosine similarity between two vectors.
     */
    private double cosineSimilarity(List<Double> vec1, List<Double> vec2) {
        if (vec1.size() != vec2.size()) {
            return 0.0;
        }
        
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;
        
        for (int i = 0; i < vec1.size(); i++) {
            dotProduct += vec1.get(i) * vec2.get(i);
            norm1 += vec1.get(i) * vec1.get(i);
            norm2 += vec2.get(i) * vec2.get(i);
        }
        
        if (norm1 == 0.0 || norm2 == 0.0) {
            return 0.0;
        }
        
        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
}