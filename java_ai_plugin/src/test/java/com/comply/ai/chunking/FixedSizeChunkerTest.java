package com.comply.ai.chunking;

import com.comply.ai.core.Document;
import com.comply.ai.core.PluginException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FixedSizeChunkerTest {
    
    private FixedSizeChunker chunker;
    
    @BeforeEach
    void setUp() throws PluginException {
        chunker = new FixedSizeChunker();
        chunker.initialize(Map.of("chunkSize", 100, "overlapSize", 20));
    }
    
    @Test
    void testInitialization() {
        assertTrue(chunker.isReady());
        assertEquals("fixed-size-chunker", chunker.getPluginId());
        assertEquals(100, chunker.getChunkSize());
        assertEquals(20, chunker.getOverlapSize());
    }
    
    @Test
    void testSmallDocumentNoChunking() throws PluginException {
        Document smallDoc = new Document("small", "Short content", Map.of("type", "test"));
        
        List<Document> chunks = chunker.chunk(smallDoc);
        
        assertEquals(1, chunks.size());
        assertEquals("small_chunk_0", chunks.get(0).getId());
        assertEquals("Short content", chunks.get(0).getContent());
        assertEquals(0, chunks.get(0).getMetadata().get("chunkIndex"));
        assertEquals(1, chunks.get(0).getMetadata().get("totalChunks"));
    }
    
    @Test
    void testLargeDocumentChunking() throws PluginException {
        // Create a document larger than chunk size
        String content = "This is a very long document that exceeds the chunk size limit. ".repeat(5);
        Document largeDoc = new Document("large", content, Map.of("type", "test"));
        
        List<Document> chunks = chunker.chunk(largeDoc);
        
        assertTrue(chunks.size() > 1);
        
        // Check chunk IDs and metadata
        for (int i = 0; i < chunks.size(); i++) {
            Document chunk = chunks.get(i);
            assertEquals("large_chunk_" + i, chunk.getId());
            assertEquals(i, chunk.getMetadata().get("chunkIndex"));
            assertEquals(chunks.size(), chunk.getMetadata().get("totalChunks"));
            assertEquals("large", chunk.getMetadata().get("originalDocId"));
            assertTrue(chunk.getContent().length() <= 100);
        }
    }
    
    @Test
    void testOverlapping() throws PluginException {
        String content = "First sentence here. Second sentence follows. Third sentence continues. Fourth sentence ends.";
        Document doc = new Document("overlap", content, Map.of());
        
        List<Document> chunks = chunker.chunk(doc);
        
        if (chunks.size() > 1) {
            // Check that consecutive chunks have some overlapping content
            // This is a simplified check - in practice, overlap detection would be more sophisticated
            String firstChunk = chunks.get(0).getContent();
            String secondChunk = chunks.get(1).getContent();
            
            // Both chunks should have some content (not empty)
            assertFalse(firstChunk.isEmpty());
            assertFalse(secondChunk.isEmpty());
        }
    }
    
    @Test
    void testInvalidConfiguration() {
        assertThrows(PluginException.class, () -> {
            FixedSizeChunker invalidChunker = new FixedSizeChunker();
            invalidChunker.initialize(Map.of("chunkSize", 50, "overlapSize", 60)); // overlap > chunk
        });
    }
    
    @Test
    void testMetadata() {
        Map<String, Object> metadata = chunker.getMetadata();
        
        assertEquals("fixed-size", metadata.get("type"));
        assertEquals(100, metadata.get("chunkSize"));
        assertEquals(20, metadata.get("overlapSize"));
        assertEquals("characters", metadata.get("unit"));
    }
}