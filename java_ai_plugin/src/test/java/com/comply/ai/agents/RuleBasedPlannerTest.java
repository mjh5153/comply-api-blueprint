package com.comply.ai.agents;

import com.comply.ai.core.PluginException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RuleBasedPlannerTest {
    
    private RuleBasedPlanner planner;
    private List<AgentTool> mockTools;
    
    @BeforeEach
    void setUp() throws PluginException {
        planner = new RuleBasedPlanner();
        planner.initialize(new HashMap<>());
        
        // Create mock tools
        mockTools = Arrays.asList(
            new MockRetrieverTool(),
            new MockCalculatorTool()
        );
    }
    
    @Test
    void testInitialization() {
        assertTrue(planner.isReady());
        assertEquals("rule-based-planner", planner.getPluginId());
    }
    
    @Test
    void testRetrievalQuery() throws PluginException {
        AgentContext context = new AgentContext("session1", "search for GDPR requirements", new HashMap<>());
        
        AgentPlan plan = planner.createPlan(context, mockTools);
        
        assertNotNull(plan);
        assertEquals("retrieval-focused", plan.getStrategy());
        assertEquals(0.9, plan.getConfidence(), 0.01);
        assertEquals(1, plan.getActions().size());
        
        PlannedAction action = plan.getActions().get(0);
        assertEquals("retrieve", action.getActionType());
        assertEquals("retriever-tool", action.getToolName());
        assertEquals(1, action.getPriority());
    }
    
    @Test
    void testCalculationQuery() throws PluginException {
        AgentContext context = new AgentContext("session2", "calculate 5 + 3", new HashMap<>());
        
        AgentPlan plan = planner.createPlan(context, mockTools);
        
        assertEquals("calculation-focused", plan.getStrategy());
        assertEquals(0.85, plan.getConfidence(), 0.01);
        assertEquals(1, plan.getActions().size());
        
        PlannedAction action = plan.getActions().get(0);
        assertEquals("calculate", action.getActionType());
        assertEquals("calculator-tool", action.getToolName());
    }
    
    @Test
    void testAnalysisQuery() throws PluginException {
        AgentContext context = new AgentContext("session3", "analyze the compliance data", new HashMap<>());
        
        AgentPlan plan = planner.createPlan(context, mockTools);
        
        assertEquals("analysis-focused", plan.getStrategy());
        assertEquals(0.75, plan.getConfidence(), 0.01);
        assertEquals(2, plan.getActions().size());
        
        // Should first retrieve, then analyze
        assertEquals("retrieve", plan.getActions().get(0).getActionType());
        assertEquals("analyze", plan.getActions().get(1).getActionType());
    }
    
    @Test
    void testDefaultQuery() throws PluginException {
        AgentContext context = new AgentContext("session4", "some random query", new HashMap<>());
        
        AgentPlan plan = planner.createPlan(context, mockTools);
        
        assertEquals("default", plan.getStrategy());
        assertEquals(0.6, plan.getConfidence(), 0.01);
        assertEquals(1, plan.getActions().size());
        assertEquals("retrieve", plan.getActions().get(0).getActionType());
    }
    
    @Test
    void testPlanValidation() throws PluginException {
        AgentContext context = new AgentContext("session1", "search test", new HashMap<>());
        AgentPlan plan = planner.createPlan(context, mockTools);
        
        assertTrue(planner.validatePlan(plan, mockTools));
    }
    
    @Test
    void testPlanValidationFailure() throws PluginException {
        AgentContext context = new AgentContext("session1", "search test", new HashMap<>());
        AgentPlan plan = planner.createPlan(context, mockTools);
        
        // Test with empty tool list
        assertFalse(planner.validatePlan(plan, Arrays.asList()));
    }
    
    // Mock tool classes for testing
    private static class MockRetrieverTool implements AgentTool {
        @Override
        public void initialize(Map<String, Object> config) {}
        
        @Override
        public ActionResult execute(Map<String, Object> parameters) {
            return ActionResult.success("mock result", 100, new HashMap<>());
        }
        
        @Override
        public ToolCapabilities getCapabilities() {
            return new ToolCapabilities("Mock retriever", Arrays.asList(), Arrays.asList(), new HashMap<>(), "Object");
        }
        
        @Override
        public boolean validateParameters(Map<String, Object> parameters) { return true; }
        
        @Override
        public String getPluginId() { return "retriever-tool"; }
        
        @Override
        public String getVersion() { return "1.0.0"; }
        
        @Override
        public Map<String, Object> getMetadata() { return new HashMap<>(); }
        
        @Override
        public boolean isReady() { return true; }
        
        @Override
        public void shutdown() {}
    }
    
    private static class MockCalculatorTool implements AgentTool {
        @Override
        public void initialize(Map<String, Object> config) {}
        
        @Override
        public ActionResult execute(Map<String, Object> parameters) {
            return ActionResult.success(8.0, 50, new HashMap<>());
        }
        
        @Override
        public ToolCapabilities getCapabilities() {
            return new ToolCapabilities("Mock calculator", Arrays.asList(), Arrays.asList(), new HashMap<>(), "Number");
        }
        
        @Override
        public boolean validateParameters(Map<String, Object> parameters) { return true; }
        
        @Override
        public String getPluginId() { return "calculator-tool"; }
        
        @Override
        public String getVersion() { return "1.0.0"; }
        
        @Override
        public Map<String, Object> getMetadata() { return new HashMap<>(); }
        
        @Override
        public boolean isReady() { return true; }
        
        @Override
        public void shutdown() {}
    }
}