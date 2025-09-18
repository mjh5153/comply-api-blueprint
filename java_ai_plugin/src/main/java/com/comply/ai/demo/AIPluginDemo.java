package com.comply.ai.demo;

import com.comply.ai.agents.*;
import com.comply.ai.chunking.FixedSizeChunker;
import com.comply.ai.core.Document;
import com.comply.ai.core.PluginException;
import com.comply.ai.evaluation.InMemoryEvaluationFramework;
import com.comply.ai.evaluation.EvaluationReport;
import com.comply.ai.retrieval.BM25Retriever;
import com.comply.ai.retrieval.HybridRetriever;
import com.comply.ai.retrieval.VectorRetriever;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Demonstration application showcasing the AI plugin system capabilities.
 */
public class AIPluginDemo {
    private static final Logger logger = LoggerFactory.getLogger(AIPluginDemo.class);
    
    public static void main(String[] args) {
        try {
            AIPluginDemo demo = new AIPluginDemo();
            demo.runFullDemo();
        } catch (Exception e) {
            logger.error("Demo failed", e);
            System.exit(1);
        }
    }
    
    public void runFullDemo() throws PluginException {
        System.out.println("=== Comply AI Plugin System Demo ===");
        System.out.println();
        
        // 1. Create sample dataset
        List<Document> sampleDocuments = createSampleDataset();
        System.out.println("📄 Created sample dataset with " + sampleDocuments.size() + " documents");
        
        // 2. Demonstrate chunking strategies
        demonstrateChunking(sampleDocuments);
        
        // 3. Demonstrate different retrieval methods
        demonstrateRetrievalMethods(sampleDocuments);
        
        // 4. Demonstrate agentic system
        demonstrateAgenticSystem(sampleDocuments);
        
        // 5. Generate evaluation report
        generateEvaluationReport();
        
        System.out.println("\n🎉 Demo completed successfully!");
    }
    
    private List<Document> createSampleDataset() {
        List<Document> documents = new ArrayList<>();
        
        // Sample compliance and business documents
        documents.add(new Document("doc_001", 
            "The General Data Protection Regulation (GDPR) is a legal framework that sets guidelines " +
            "for the collection and processing of personal information from individuals who live in the " +
            "European Union (EU). GDPR came into effect on May 25, 2018. Companies must obtain explicit " +
            "consent from users before collecting personal data and must provide users with the ability " +
            "to access, modify, or delete their personal information.",
            Map.of("type", "regulation", "category", "privacy", "region", "EU")));
        
        documents.add(new Document("doc_002",
            "Financial institutions must comply with Anti-Money Laundering (AML) regulations which " +
            "require them to monitor customer transactions for suspicious activities. Key requirements " +
            "include customer due diligence, transaction monitoring, suspicious activity reporting, " +
            "and maintaining comprehensive records. Penalties for non-compliance can include significant " +
            "fines and regulatory sanctions.",
            Map.of("type", "regulation", "category", "financial", "region", "US")));
        
        documents.add(new Document("doc_003",
            "Our company's data retention policy specifies that customer data must be retained for " +
            "a minimum of 7 years for accounting purposes, but no longer than necessary for the " +
            "purposes for which it was collected. Personal data of EU residents must be deleted " +
            "within 30 days of a valid erasure request under GDPR Article 17. All data deletions " +
            "must be logged and auditable.",
            Map.of("type", "policy", "category", "data-retention", "scope", "internal")));
        
        documents.add(new Document("doc_004",
            "Risk assessment procedures require quarterly evaluation of all third-party vendors. " +
            "The assessment must include security posture evaluation, compliance verification, " +
            "financial stability review, and business continuity planning assessment. Any vendor " +
            "with a risk score above 7 must undergo additional due diligence and may require " +
            "remediation before contract renewal.",
            Map.of("type", "procedure", "category", "risk-management", "frequency", "quarterly")));
        
        documents.add(new Document("doc_005",
            "Incident response procedures mandate that all security incidents must be reported " +
            "within 24 hours to the security team. Critical incidents affecting customer data " +
            "must be escalated to executive leadership within 2 hours. For GDPR-covered incidents, " +
            "we must notify relevant supervisory authorities within 72 hours and affected individuals " +
            "without undue delay if high risk to their rights and freedoms.",
            Map.of("type", "procedure", "category", "security", "urgency", "high")));
        
        documents.add(new Document("doc_006",
            "Employee training requirements mandate annual compliance training for all staff, " +
            "with specialized training for roles handling sensitive data. Training covers data " +
            "protection principles, security awareness, incident reporting, and regulatory " +
            "requirements. Completion must be tracked and documented for audit purposes. " +
            "New employees must complete training within 30 days of starting.",
            Map.of("type", "policy", "category", "training", "frequency", "annual")));
        
        return documents;
    }
    
    private void demonstrateChunking(List<Document> documents) throws PluginException {
        System.out.println("\n📋 Demonstrating Chunking Strategies");
        System.out.println("-".repeat(40));
        
        // Test with different chunk sizes
        int[] chunkSizes = {200, 300, 500};
        
        for (int chunkSize : chunkSizes) {
            FixedSizeChunker chunker = new FixedSizeChunker();
            Map<String, Object> config = Map.of("chunkSize", chunkSize, "overlapSize", chunkSize / 10);
            chunker.initialize(config);
            
            List<Document> allChunks = chunker.chunkAll(documents);
            System.out.printf("Chunk size %d: %d documents → %d chunks%n", 
                            chunkSize, documents.size(), allChunks.size());
            
            chunker.shutdown();
        }
    }
    
    private void demonstrateRetrievalMethods(List<Document> documents) throws PluginException {
        System.out.println("\n🔍 Demonstrating Retrieval Methods");
        System.out.println("-".repeat(40));
        
        String[] testQueries = {
            "GDPR data protection requirements",
            "financial compliance monitoring",
            "incident response procedures",
            "employee training requirements"
        };
        
        // Test BM25 Retriever
        System.out.println("\n📊 BM25 Retrieval Results:");
        BM25Retriever bm25 = new BM25Retriever();
        bm25.initialize(new HashMap<>());
        bm25.index(documents);
        
        for (String query : testQueries) {
            var results = bm25.retrieve(query, 3);
            System.out.printf("Query: '%s' → %d results%n", query, results.size());
            results.forEach(r -> System.out.printf("  - Score: %.3f, Doc: %s%n", 
                                                 r.getScore(), r.getDocument().getId()));
        }
        
        // Test Vector Retriever
        System.out.println("\n🧮 Vector Retrieval Results:");
        VectorRetriever vector = new VectorRetriever();
        vector.initialize(new HashMap<>());
        vector.index(documents);
        
        for (String query : testQueries) {
            var results = vector.retrieve(query, 3);
            System.out.printf("Query: '%s' → %d results%n", query, results.size());
            results.forEach(r -> System.out.printf("  - Score: %.3f, Doc: %s%n", 
                                                 r.getScore(), r.getDocument().getId()));
        }
        
        // Test Hybrid Retriever
        System.out.println("\n🔀 Hybrid Retrieval Results:");
        HybridRetriever hybrid = new HybridRetriever(
            Arrays.asList(bm25, vector), 
            Map.of("bm25-retriever", 0.6, "vector-retriever", 0.4)
        );
        hybrid.initialize(new HashMap<>());
        
        for (String query : testQueries) {
            var results = hybrid.retrieve(query, 3);
            System.out.printf("Query: '%s' → %d results%n", query, results.size());
            results.forEach(r -> System.out.printf("  - Score: %.3f, Doc: %s%n", 
                                                 r.getScore(), r.getDocument().getId()));
        }
        
        bm25.shutdown();
        vector.shutdown();
        hybrid.shutdown();
    }
    
    private void demonstrateAgenticSystem(List<Document> documents) throws PluginException {
        System.out.println("\n🤖 Demonstrating Agentic System");
        System.out.println("-".repeat(40));
        
        // Set up retrieval system
        BM25Retriever retriever = new BM25Retriever();
        retriever.initialize(new HashMap<>());
        retriever.index(documents);
        
        // Set up agent components
        PlanningModule planner = new RuleBasedPlanner();
        planner.initialize(new HashMap<>());
        
        ExecutionModule executor = new SequentialExecutor();
        executor.initialize(Map.of("continueOnFailure", true));
        
        List<AgentTool> tools = Arrays.asList(
            new RetrieverTool(retriever),
            new CalculatorTool()
        );
        
        // Initialize tools
        for (AgentTool tool : tools) {
            tool.initialize(new HashMap<>());
        }
        
        InMemoryEvaluationFramework evaluator = new InMemoryEvaluationFramework();
        evaluator.initialize(new HashMap<>());
        
        // Test queries that demonstrate different agent behaviors
        String[] agentQueries = {
            "Find information about GDPR compliance requirements",
            "What are the incident response time requirements?",
            "Calculate 72 * 24 hours for compliance reporting",
            "Search for employee training policies and procedures"
        };
        
        for (int i = 0; i < agentQueries.length; i++) {
            String sessionId = "session_" + (i + 1);
            String query = agentQueries[i];
            
            System.out.printf("\n💭 Processing Query %d: '%s'%n", i + 1, query);
            
            // Create agent context
            AgentContext context = new AgentContext(sessionId, query, new HashMap<>());
            
            // Planning phase
            long planningStart = System.currentTimeMillis();
            AgentPlan plan = planner.createPlan(context, tools);
            long planningTime = System.currentTimeMillis() - planningStart;
            
            System.out.printf("   📋 Plan: %s (confidence: %.2f, %d actions)%n", 
                            plan.getStrategy(), plan.getConfidence(), plan.getActions().size());
            
            // Execution phase
            long executionStart = System.currentTimeMillis();
            List<ActionResult> results = executor.executePlan(plan, tools, context);
            long executionTime = System.currentTimeMillis() - executionStart;
            
            int successful = (int) results.stream().mapToInt(r -> r.isSuccess() ? 1 : 0).sum();
            System.out.printf("   ✅ Execution: %d/%d actions successful (%.1fms)%n", 
                            successful, results.size(), (double) executionTime);
            
            // Record for evaluation
            evaluator.recordExecution(context, plan, results, planningTime, executionTime);
        }
        
        // Cleanup
        for (AgentTool tool : tools) {
            tool.shutdown();
        }
        planner.shutdown();
        executor.shutdown();
        retriever.shutdown();
    }
    
    private void generateEvaluationReport() throws PluginException {
        System.out.println("\n📊 Evaluation Report");
        System.out.println("-".repeat(40));
        
        InMemoryEvaluationFramework evaluator = new InMemoryEvaluationFramework();
        evaluator.initialize(new HashMap<>());
        
        // Generate some sample data for the report
        for (int i = 0; i < 10; i++) {
            AgentContext context = new AgentContext("eval_session_" + i, 
                                                  "Sample query " + i, new HashMap<>());
            AgentPlan plan = new AgentPlan("plan_" + i, new ArrayList<>(), "test", 0.8 + (i % 3) * 0.1);
            List<ActionResult> results = Arrays.asList(
                ActionResult.success("result", 100 + i * 10, new HashMap<>())
            );
            
            evaluator.recordExecution(context, plan, results, 50 + i * 5, 200 + i * 20);
        }
        
        EvaluationReport report = evaluator.generateReport(50);
        
        System.out.println("Summary: " + report.getSummary());
        System.out.println("\nKey Insights:");
        report.getInsights().forEach(insight -> System.out.println("  • " + insight));
        System.out.println("\nRecommendations:");
        report.getRecommendations().forEach(rec -> System.out.println("  • " + rec));
        
        System.out.println("\nDetailed Metrics:");
        System.out.println("  " + report.getMetrics());
        
        evaluator.shutdown();
    }
}