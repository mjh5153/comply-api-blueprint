package com.comply.ai.retrieval;

import com.comply.ai.core.Document;
import com.comply.ai.core.PluginException;
import com.comply.ai.core.RetrievalResult;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BM25-based retrieval implementation using Apache Lucene.
 * Provides term-based retrieval with TF-IDF scoring.
 */
public class BM25Retriever implements Retriever {
    private static final Logger logger = LoggerFactory.getLogger(BM25Retriever.class);
    
    private static final String PLUGIN_ID = "bm25-retriever";
    private static final String VERSION = "1.0.0";
    
    private Directory directory;
    private Analyzer analyzer;
    private IndexWriter indexWriter;
    private Map<String, Document> documentStore;
    private boolean isInitialized = false;
    
    @Override
    public void initialize(Map<String, Object> config) throws PluginException {
        try {
            this.directory = new ByteBuffersDirectory();
            this.analyzer = new StandardAnalyzer();
            IndexWriterConfig iwConfig = new IndexWriterConfig(analyzer);
            this.indexWriter = new IndexWriter(directory, iwConfig);
            this.documentStore = new HashMap<>();
            this.isInitialized = true;
            
            logger.info("Initialized BM25Retriever with config: {}", config);
        } catch (IOException e) {
            throw new PluginException("Failed to initialize BM25Retriever", e);
        }
    }
    
    @Override
    public void index(List<Document> documents) throws PluginException {
        if (!isInitialized) {
            throw new PluginException("BM25Retriever not initialized");
        }
        
        try {
            for (Document doc : documents) {
                org.apache.lucene.document.Document luceneDoc = new org.apache.lucene.document.Document();
                luceneDoc.add(new StringField("id", doc.getId(), Field.Store.YES));
                luceneDoc.add(new TextField("content", doc.getContent(), Field.Store.YES));
                
                // Add metadata fields
                for (Map.Entry<String, Object> entry : doc.getMetadata().entrySet()) {
                    luceneDoc.add(new TextField("meta_" + entry.getKey(), 
                                              entry.getValue().toString(), Field.Store.YES));
                }
                
                indexWriter.addDocument(luceneDoc);
                documentStore.put(doc.getId(), doc);
            }
            indexWriter.commit();
            
            logger.info("Indexed {} documents", documents.size());
        } catch (IOException e) {
            throw new PluginException("Failed to index documents", e);
        }
    }
    
    @Override
    public List<RetrievalResult> retrieve(String query, int maxResults) throws PluginException {
        if (!isInitialized) {
            throw new PluginException("BM25Retriever not initialized");
        }
        
        List<RetrievalResult> results = new ArrayList<>();
        
        try (IndexReader reader = DirectoryReader.open(directory)) {
            IndexSearcher searcher = new IndexSearcher(reader);
            QueryParser parser = new QueryParser("content", analyzer);
            
            Query luceneQuery = parser.parse(QueryParser.escape(query));
            TopDocs topDocs = searcher.search(luceneQuery, maxResults);
            
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                org.apache.lucene.document.Document luceneDoc = searcher.doc(scoreDoc.doc);
                String docId = luceneDoc.get("id");
                Document originalDoc = documentStore.get(docId);
                
                if (originalDoc != null) {
                    results.add(new RetrievalResult(originalDoc, scoreDoc.score, PLUGIN_ID));
                }
            }
            
            logger.debug("Retrieved {} results for query: {}", results.size(), query);
        } catch (IOException | ParseException e) {
            throw new PluginException("Failed to retrieve documents", e);
        }
        
        return results;
    }
    
    @Override
    public int getIndexedDocumentCount() {
        return documentStore.size();
    }
    
    @Override
    public void clearIndex() {
        try {
            indexWriter.deleteAll();
            indexWriter.commit();
            documentStore.clear();
            logger.info("Cleared index");
        } catch (IOException e) {
            logger.error("Failed to clear index", e);
        }
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
        metadata.put("type", "term-based");
        metadata.put("algorithm", "BM25");
        metadata.put("indexedDocuments", getIndexedDocumentCount());
        return metadata;
    }
    
    @Override
    public boolean isReady() {
        return isInitialized;
    }
    
    @Override
    public void shutdown() {
        try {
            if (indexWriter != null) {
                indexWriter.close();
            }
            if (directory != null) {
                directory.close();
            }
            isInitialized = false;
            logger.info("BM25Retriever shutdown complete");
        } catch (IOException e) {
            logger.error("Error during shutdown", e);
        }
    }
}