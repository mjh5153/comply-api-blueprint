package com.comply.ai.chunking;

import com.comply.ai.core.Document;
import com.comply.ai.core.PluginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Simple fixed-size chunking strategy with configurable overlap.
 */
public class FixedSizeChunker implements ChunkingStrategy {
    private static final Logger logger = LoggerFactory.getLogger(FixedSizeChunker.class);
    
    private static final String PLUGIN_ID = "fixed-size-chunker";
    private static final String VERSION = "1.0.0";
    
    private int chunkSize = 500; // characters
    private int overlapSize = 50; // characters
    private boolean isInitialized = false;
    
    @Override
    public void initialize(Map<String, Object> config) throws PluginException {
        if (config != null) {
            this.chunkSize = (Integer) config.getOrDefault("chunkSize", 500);
            this.overlapSize = (Integer) config.getOrDefault("overlapSize", 50);
        }
        
        if (overlapSize >= chunkSize) {
            throw new PluginException("Overlap size must be less than chunk size");
        }
        
        this.isInitialized = true;
        logger.info("Initialized FixedSizeChunker: chunkSize={}, overlapSize={}", chunkSize, overlapSize);
    }
    
    @Override
    public List<Document> chunk(Document document) throws PluginException {
        if (!isInitialized) {
            throw new PluginException("FixedSizeChunker not initialized");
        }
        
        String content = document.getContent();
        List<Document> chunks = new ArrayList<>();
        
        if (content.length() <= chunkSize) {
            // Document is small enough, return as single chunk
            Map<String, Object> chunkMetadata = new HashMap<>(document.getMetadata());
            chunkMetadata.put("chunkIndex", 0);
            chunkMetadata.put("totalChunks", 1);
            chunkMetadata.put("originalDocId", document.getId());
            
            chunks.add(new Document(
                    document.getId() + "_chunk_0",
                    content,
                    chunkMetadata
            ));
            return chunks;
        }
        
        int stepSize = chunkSize - overlapSize;
        int chunkIndex = 0;
        
        for (int start = 0; start < content.length(); start += stepSize) {
            int end = Math.min(start + chunkSize, content.length());
            String chunkContent = content.substring(start, end);
            
            // Try to break at word boundaries to avoid cutting words
            if (end < content.length() && !Character.isWhitespace(content.charAt(end))) {
                int lastSpace = chunkContent.lastIndexOf(' ');
                if (lastSpace > chunkContent.length() / 2) { // Only break if space is in latter half
                    chunkContent = chunkContent.substring(0, lastSpace);
                }
            }
            
            Map<String, Object> chunkMetadata = new HashMap<>(document.getMetadata());
            chunkMetadata.put("chunkIndex", chunkIndex);
            chunkMetadata.put("originalDocId", document.getId());
            chunkMetadata.put("startPosition", start);
            chunkMetadata.put("endPosition", start + chunkContent.length());
            
            chunks.add(new Document(
                    document.getId() + "_chunk_" + chunkIndex,
                    chunkContent,
                    chunkMetadata
            ));
            
            chunkIndex++;
            
            // Stop if we've reached the end
            if (start + chunkContent.length() >= content.length()) {
                break;
            }
        }
        
        // Update total chunks count in metadata
        for (Document chunk : chunks) {
            chunk.getMetadata().put("totalChunks", chunks.size());
        }
        
        logger.debug("Chunked document {} into {} pieces", document.getId(), chunks.size());
        return chunks;
    }
    
    @Override
    public List<Document> chunkAll(List<Document> documents) throws PluginException {
        List<Document> allChunks = new ArrayList<>();
        
        for (Document doc : documents) {
            allChunks.addAll(chunk(doc));
        }
        
        logger.info("Chunked {} documents into {} total chunks", documents.size(), allChunks.size());
        return allChunks;
    }
    
    @Override
    public int getChunkSize() {
        return chunkSize;
    }
    
    @Override
    public int getOverlapSize() {
        return overlapSize;
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
        metadata.put("type", "fixed-size");
        metadata.put("chunkSize", chunkSize);
        metadata.put("overlapSize", overlapSize);
        metadata.put("unit", "characters");
        return metadata;
    }
    
    @Override
    public boolean isReady() {
        return isInitialized;
    }
    
    @Override
    public void shutdown() {
        isInitialized = false;
        logger.info("FixedSizeChunker shutdown complete");
    }
}