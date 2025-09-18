package com.comply.ai.retrieval;

import com.comply.ai.core.Document;
import com.comply.ai.core.PluginException;
import com.comply.ai.core.RetrievalResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BM25RetrieverTest {
    
    private BM25Retriever retriever;
    private List<Document> testDocuments;
    
    @BeforeEach
    void setUp() throws PluginException {
        retriever = new BM25Retriever();
        retriever.initialize(new HashMap<>());
        
        testDocuments = Arrays.asList(
            new Document("doc1", "GDPR data protection regulation privacy", 
                        Map.of("type", "regulation")),
            new Document("doc2", "Financial compliance monitoring anti-money laundering", 
                        Map.of("type", "policy")),
            new Document("doc3", "Security incident response procedures emergency", 
                        Map.of("type", "procedure"))
        );
        
        retriever.index(testDocuments);
    }
    
    @Test
    void testInitialization() {
        assertTrue(retriever.isReady());
        assertEquals("bm25-retriever", retriever.getPluginId());
        assertEquals("1.0.0", retriever.getVersion());
    }
    
    @Test
    void testIndexing() {
        assertEquals(3, retriever.getIndexedDocumentCount());
    }
    
    @Test
    void testRetrieval() throws PluginException {
        List<RetrievalResult> results = retriever.retrieve("GDPR privacy", 2);
        
        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertTrue(results.size() <= 2);
        
        // First result should be most relevant
        RetrievalResult topResult = results.get(0);
        assertEquals("doc1", topResult.getDocument().getId());
        assertTrue(topResult.getScore() > 0);
        assertEquals("bm25-retriever", topResult.getRetrievalMethod());
    }
    
    @Test
    void testRetrievalWithMaxResults() throws PluginException {
        List<RetrievalResult> results = retriever.retrieve("regulation", 1);
        assertEquals(1, results.size());
    }
    
    @Test
    void testRetrievalNoResults() throws PluginException {
        List<RetrievalResult> results = retriever.retrieve("nonexistent term", 5);
        assertTrue(results.isEmpty());
    }
    
    @Test
    void testClearIndex() {
        retriever.clearIndex();
        assertEquals(0, retriever.getIndexedDocumentCount());
    }
    
    @Test
    void testMetadata() {
        Map<String, Object> metadata = retriever.getMetadata();
        
        assertEquals("term-based", metadata.get("type"));
        assertEquals("BM25", metadata.get("algorithm"));
        assertEquals(3, metadata.get("indexedDocuments"));
    }
    
    @Test
    void testShutdown() {
        retriever.shutdown();
        assertFalse(retriever.isReady());
    }
}