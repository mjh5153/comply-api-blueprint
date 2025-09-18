package com.comply.ai.core;

import java.util.List;
import java.util.Map;

/**
 * Represents a document with content, metadata, and embeddings.
 */
public class Document {
    private final String id;
    private final String content;
    private final Map<String, Object> metadata;
    private final List<Double> embedding;
    
    public Document(String id, String content, Map<String, Object> metadata) {
        this(id, content, metadata, null);
    }
    
    public Document(String id, String content, Map<String, Object> metadata, List<Double> embedding) {
        this.id = id;
        this.content = content;
        this.metadata = metadata;
        this.embedding = embedding;
    }
    
    public String getId() {
        return id;
    }
    
    public String getContent() {
        return content;
    }
    
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    public List<Double> getEmbedding() {
        return embedding;
    }
    
    public Document withEmbedding(List<Double> embedding) {
        return new Document(id, content, metadata, embedding);
    }
    
    @Override
    public String toString() {
        return "Document{" +
                "id='" + id + '\'' +
                ", content='" + content.substring(0, Math.min(50, content.length())) + "...'" +
                ", metadata=" + metadata +
                ", hasEmbedding=" + (embedding != null) +
                '}';
    }
}