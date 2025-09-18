package com.comply.ai.retrieval;

import com.comply.ai.core.Document;
import com.comply.ai.core.Plugin;
import com.comply.ai.core.PluginException;
import com.comply.ai.core.RetrievalResult;

import java.util.List;

/**
 * Interface for retrieval strategies that can find relevant documents based on queries.
 */
public interface Retriever extends Plugin {
    
    /**
     * Index documents for retrieval.
     * 
     * @param documents List of documents to index
     * @throws PluginException if indexing fails
     */
    void index(List<Document> documents) throws PluginException;
    
    /**
     * Retrieve relevant documents for a query.
     * 
     * @param query The search query
     * @param maxResults Maximum number of results to return
     * @return List of retrieval results ordered by relevance
     * @throws PluginException if retrieval fails
     */
    List<RetrievalResult> retrieve(String query, int maxResults) throws PluginException;
    
    /**
     * Get the total number of indexed documents.
     * 
     * @return Number of indexed documents
     */
    int getIndexedDocumentCount();
    
    /**
     * Clear all indexed documents.
     */
    void clearIndex();
}