package com.comply.ai.core;

/**
 * Represents the result of a retrieval operation with relevance score.
 */
public class RetrievalResult {
    private final Document document;
    private final double score;
    private final String retrievalMethod;
    
    public RetrievalResult(Document document, double score, String retrievalMethod) {
        this.document = document;
        this.score = score;
        this.retrievalMethod = retrievalMethod;
    }
    
    public Document getDocument() {
        return document;
    }
    
    public double getScore() {
        return score;
    }
    
    public String getRetrievalMethod() {
        return retrievalMethod;
    }
    
    @Override
    public String toString() {
        return "RetrievalResult{" +
                "document=" + document +
                ", score=" + score +
                ", retrievalMethod='" + retrievalMethod + '\'' +
                '}';
    }
}