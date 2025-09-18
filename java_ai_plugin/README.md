# Comply AI Plugin System

A flexible Java-based AI plugin system designed for integration into company applications, delivering tailored value to each line of business (LOB). The architecture enables rapid iteration and extensibility by internal AI engineers, leveraging modern retrieval and agentic AI patterns.

## Architecture Overview

The system is built around a modular plugin architecture with clear separation of concerns:

```
┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐
│   Retrieval     │  │   Chunking      │  │   Agents        │
│   Strategies    │  │   Strategies    │  │   System        │
├─────────────────┤  ├─────────────────┤  ├─────────────────┤
│ • BM25/TF-IDF   │  │ • Fixed Size    │  │ • Planning      │
│ • Vector-based  │  │ • Semantic      │  │ • Execution     │
│ • Hybrid        │  │ • Overlapping   │  │ • Tools         │
└─────────────────┘  └─────────────────┘  └─────────────────┘
                                          ┌─────────────────┐
                                          │   Evaluation    │
                                          │   Framework     │
                                          ├─────────────────┤
                                          │ • Metrics       │
                                          │ • Reports       │
                                          │ • Analytics     │
                                          └─────────────────┘
```

## Core Components

### 1. Plugin System (`com.comply.ai.core`)

The foundation of the system, providing:
- **Plugin interface**: Base interface for all system components
- **Document model**: Unified document representation with metadata and embeddings
- **RetrievalResult**: Results from retrieval operations with scoring
- **Exception handling**: Consistent error handling across the system

### 2. Retrieval Strategies (`com.comply.ai.retrieval`)

Multiple retrieval approaches for different use cases:

#### BM25Retriever
- **Use case**: Term-based search, keyword matching
- **Technology**: Apache Lucene with BM25 scoring
- **Strengths**: Fast, interpretable, works well for exact matches
- **Configuration**: Standard analyzer, customizable fields

#### VectorRetriever  
- **Use case**: Semantic similarity, conceptual matching
- **Technology**: Cosine similarity with mock embeddings
- **Strengths**: Understands context and meaning
- **Configuration**: Embedding dimension, similarity threshold

#### HybridRetriever
- **Use case**: Best of both worlds - precision and recall
- **Technology**: Weighted score fusion from multiple retrievers
- **Strengths**: Combines strengths of different approaches
- **Configuration**: Component retrievers and their weights

### 3. Chunking Strategies (`com.comply.ai.chunking`)

Document preprocessing for optimal retrieval:

#### FixedSizeChunker
- **Strategy**: Split documents into fixed-size chunks with overlap
- **Configuration**: Chunk size (characters), overlap size
- **Features**: Word boundary preservation, metadata enrichment
- **Use case**: General-purpose chunking for most content types

### 4. Agentic Architecture (`com.comply.ai.agents`)

Separated planning and execution for flexible agent behavior:

#### Planning Module (RuleBasedPlanner)
- **Function**: Analyze queries and create execution plans
- **Strategy**: Pattern matching on query intent
- **Output**: Ordered list of actions with confidence scores
- **Extensibility**: Easy to add new query patterns and strategies

#### Execution Module (SequentialExecutor)
- **Function**: Execute planned actions using available tools
- **Strategy**: Sequential execution with error handling
- **Features**: Continue on failure, timeout handling
- **Monitoring**: Execution metrics and performance tracking

#### Agent Tools
- **RetrieverTool**: Interface to retrieval systems
- **CalculatorTool**: Basic mathematical operations
- **Extensible**: Easy to add new tools (SQL, APIs, etc.)

### 5. Evaluation Framework (`com.comply.ai.evaluation`)

Comprehensive metrics collection and analysis:

#### Metrics Collected
- **Planning validity**: Success rate, confidence levels
- **Tool success rate**: Individual and aggregate performance
- **Efficiency**: Execution times, resource usage
- **Quality**: Result relevance and completeness

#### Reporting
- **Real-time**: Execution metrics as they happen
- **Aggregated**: Trends and patterns over time
- **Insights**: Automated analysis and recommendations

## Quick Start

### 1. Dependencies

```xml
<dependency>
    <groupId>com.comply</groupId>
    <artifactId>ai-plugin</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. Basic Usage

```java
// Create and configure retriever
BM25Retriever retriever = new BM25Retriever();
retriever.initialize(new HashMap<>());

// Index documents
List<Document> documents = Arrays.asList(
    new Document("doc1", "Content here", Map.of("type", "policy"))
);
retriever.index(documents);

// Retrieve relevant documents
List<RetrievalResult> results = retriever.retrieve("search query", 5);
```

### 3. Agent System

```java
// Set up agent components
PlanningModule planner = new RuleBasedPlanner();
ExecutionModule executor = new SequentialExecutor();
List<AgentTool> tools = Arrays.asList(
    new RetrieverTool(retriever),
    new CalculatorTool()
);

// Initialize components
planner.initialize(new HashMap<>());
executor.initialize(new HashMap<>());

// Create context and execute
AgentContext context = new AgentContext("session1", "Find GDPR requirements", new HashMap<>());
AgentPlan plan = planner.createPlan(context, tools);
List<ActionResult> results = executor.executePlan(plan, tools, context);
```

## Extension Points

### Adding New Retrievers

1. Implement the `Retriever` interface
2. Handle initialization, indexing, and retrieval
3. Provide metadata about capabilities

```java
public class CustomRetriever implements Retriever {
    @Override
    public void initialize(Map<String, Object> config) { /* ... */ }
    
    @Override
    public List<RetrievalResult> retrieve(String query, int maxResults) {
        // Custom retrieval logic
        return results;
    }
    // ... other methods
}
```

### Adding New Agent Tools

1. Implement the `AgentTool` interface
2. Define capabilities and parameter validation
3. Handle execution logic

```java
public class SQLTool implements AgentTool {
    @Override
    public ActionResult execute(Map<String, Object> parameters) {
        String sql = (String) parameters.get("query");
        // Execute SQL and return results
        return ActionResult.success(results, executionTime, metadata);
    }
    
    @Override
    public ToolCapabilities getCapabilities() {
        return new ToolCapabilities(
            "Execute SQL queries",
            Arrays.asList("query"),
            Arrays.asList("limit"),
            paramDescriptions,
            "ResultSet"
        );
    }
}
```

### Adding New Chunking Strategies

1. Implement the `ChunkingStrategy` interface
2. Define chunking logic and configuration
3. Handle metadata preservation

```java
public class SemanticChunker implements ChunkingStrategy {
    @Override
    public List<Document> chunk(Document document) {
        // Semantic boundary detection
        // Create chunks based on topic changes
        return chunks;
    }
}
```

## Configuration

### Retrieval Configuration

```java
Map<String, Object> config = Map.of(
    "indexPath", "/path/to/index",
    "analyzer", "standard",
    "similarity", "BM25"
);
```

### Chunking Configuration

```java
Map<String, Object> config = Map.of(
    "chunkSize", 500,
    "overlapSize", 50,
    "preserveWordBoundaries", true
);
```

### Agent Configuration

```java
Map<String, Object> config = Map.of(
    "continueOnFailure", true,
    "maxExecutionTime", 30000,
    "defaultPlanningStrategy", "rule-based"
);
```

## Performance Considerations

### Indexing
- **Batch processing**: Index documents in batches for efficiency
- **Memory usage**: Monitor memory consumption during indexing
- **Index optimization**: Regular index optimization for query performance

### Retrieval
- **Caching**: Implement result caching for frequently accessed queries
- **Parallel processing**: Use parallel retrieval for multiple sources
- **Result limits**: Set appropriate limits to prevent resource exhaustion

### Agent Execution
- **Timeout handling**: Set reasonable timeouts for tool execution
- **Resource monitoring**: Track memory and CPU usage during execution
- **Plan optimization**: Optimize plan complexity based on available resources

## Monitoring and Evaluation

The system includes comprehensive monitoring capabilities:

### Key Metrics
- **Success Rate**: Percentage of successful executions
- **Response Time**: Average execution time per query
- **Plan Confidence**: Quality of planning decisions
- **Tool Performance**: Individual tool success rates

### Evaluation Dataset
The system supports evaluation with organic datasets (50-100 turns):
- **Ground Truth**: Expected results for benchmark queries
- **Performance Tracking**: Historical performance trends
- **A/B Testing**: Compare different configurations

### Reporting
- **Real-time Dashboards**: Live system performance
- **Weekly Reports**: Trend analysis and recommendations
- **Alert System**: Notifications for performance degradation

## Integration Examples

### Spring Boot Integration

```java
@Configuration
public class AIPluginConfig {
    
    @Bean
    public Retriever primaryRetriever() {
        BM25Retriever retriever = new BM25Retriever();
        retriever.initialize(retrievalConfig());
        return retriever;
    }
    
    @Bean
    public PlanningModule planner() {
        RuleBasedPlanner planner = new RuleBasedPlanner();
        planner.initialize(planningConfig());
        return planner;
    }
}
```

### REST API Integration

```java
@RestController
@RequestMapping("/api/ai")
public class AIController {
    
    @Autowired
    private AIPluginService aiService;
    
    @PostMapping("/search")
    public ResponseEntity<SearchResponse> search(@RequestBody SearchRequest request) {
        List<RetrievalResult> results = aiService.search(request.getQuery());
        return ResponseEntity.ok(new SearchResponse(results));
    }
    
    @PostMapping("/agent")
    public ResponseEntity<AgentResponse> executeAgent(@RequestBody AgentRequest request) {
        AgentResponse response = aiService.executeAgent(request);
        return ResponseEntity.ok(response);
    }
}
```

## Best Practices

### Security
- **Input Validation**: Always validate user inputs
- **Sanitization**: Sanitize queries and parameters
- **Access Control**: Implement proper authentication and authorization
- **Audit Logging**: Log all system interactions for security analysis

### Performance
- **Connection Pooling**: Use connection pools for external services
- **Async Processing**: Use asynchronous processing for long-running operations
- **Resource Management**: Properly manage and clean up resources
- **Load Balancing**: Distribute load across multiple instances

### Reliability
- **Error Handling**: Implement comprehensive error handling
- **Retry Logic**: Add retry mechanisms for transient failures
- **Circuit Breakers**: Use circuit breakers for external service calls
- **Health Checks**: Implement health check endpoints

## Troubleshooting

### Common Issues

#### IndexNotFoundException
**Cause**: Retriever not properly initialized or documents not indexed
**Solution**: Ensure `initialize()` and `index()` are called before retrieval

#### PluginNotReadyException
**Cause**: Plugin used before initialization
**Solution**: Check `isReady()` before using plugins

#### Low Retrieval Scores
**Cause**: Query-document mismatch or poor indexing
**Solution**: Review query preprocessing and document chunking strategy

#### Agent Plan Failures
**Cause**: Tools not available or parameter validation failures
**Solution**: Verify tool registration and parameter schemas

### Performance Issues

#### Slow Retrieval
- Check index size and optimization
- Consider result caching
- Review query complexity

#### High Memory Usage
- Monitor document indexing batch sizes
- Check for memory leaks in plugins
- Optimize document storage

#### Agent Timeouts
- Review tool execution times
- Optimize plan complexity
- Consider async execution

## API Reference

See the JavaDoc documentation for complete API reference:
- [Plugin API](api/Plugin.html)
- [Retrieval API](api/Retriever.html)
- [Agent API](api/Agent.html)
- [Evaluation API](api/Evaluation.html)

## Contributing

### Development Setup

1. Clone the repository
2. Run `mvn compile` to build
3. Run tests with `mvn test`
4. Run demo with `mvn exec:java`

### Code Style
- Follow Google Java Style Guide
- Use meaningful variable and method names
- Include comprehensive JavaDoc comments
- Write unit tests for new functionality

### Pull Requests
- Create feature branches from main
- Include tests for new functionality
- Update documentation as needed
- Ensure all tests pass before submission

## License

This project is licensed under the MIT License - see the LICENSE file for details.