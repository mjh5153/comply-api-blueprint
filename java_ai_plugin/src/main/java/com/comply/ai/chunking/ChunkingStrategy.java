package com.comply.ai.chunking;

import com.comply.ai.core.Document;
import com.comply.ai.core.Plugin;
import com.comply.ai.core.PluginException;

import java.util.List;

/**
 * Interface for chunking strategies that split documents into smaller, manageable pieces.
 */
public interface ChunkingStrategy extends Plugin {
    
    /**
     * Chunk a document into smaller pieces.
     * 
     * @param document The document to chunk
     * @return List of document chunks
     * @throws PluginException if chunking fails
     */
    List<Document> chunk(Document document) throws PluginException;
    
    /**
     * Chunk multiple documents.
     * 
     * @param documents List of documents to chunk
     * @return List of all chunks from all documents
     * @throws PluginException if chunking fails
     */
    List<Document> chunkAll(List<Document> documents) throws PluginException;
    
    /**
     * Get the configured chunk size.
     * 
     * @return Chunk size in characters/tokens
     */
    int getChunkSize();
    
    /**
     * Get the configured overlap size.
     * 
     * @return Overlap size in characters/tokens
     */
    int getOverlapSize();
}