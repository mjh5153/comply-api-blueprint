package com.comply.ai.agents;

import com.comply.ai.core.PluginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Agent tool that provides basic calculator capabilities.
 */
public class CalculatorTool implements AgentTool {
    private static final Logger logger = LoggerFactory.getLogger(CalculatorTool.class);
    
    private static final String PLUGIN_ID = "calculator-tool";
    private static final String VERSION = "1.0.0";
    
    private boolean isInitialized = false;
    
    @Override
    public void initialize(Map<String, Object> config) throws PluginException {
        this.isInitialized = true;
        logger.info("Initialized CalculatorTool");
    }
    
    @Override
    public ActionResult execute(Map<String, Object> parameters) throws PluginException {
        if (!isInitialized) {
            throw new PluginException("CalculatorTool not initialized");
        }
        
        long startTime = System.currentTimeMillis();
        
        try {
            String expression = (String) parameters.get("expression");
            
            if (expression == null || expression.trim().isEmpty()) {
                return ActionResult.failure("Expression parameter is required",
                                          System.currentTimeMillis() - startTime);
            }
            
            double result = evaluateSimpleExpression(expression);
            
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("originalExpression", expression);
            metadata.put("resultType", "Double");
            
            return ActionResult.success(result, System.currentTimeMillis() - startTime, metadata);
            
        } catch (Exception e) {
            return ActionResult.failure("Calculation error: " + e.getMessage(),
                                      System.currentTimeMillis() - startTime);
        }
    }
    
    @Override
    public ToolCapabilities getCapabilities() {
        return new ToolCapabilities(
            "Perform basic mathematical calculations",
            Arrays.asList("expression"),
            Collections.emptyList(),
            Map.of("expression", "Mathematical expression to evaluate (e.g., '2 + 3 * 4')"),
            "Number"
        );
    }
    
    @Override
    public boolean validateParameters(Map<String, Object> parameters) {
        if (parameters == null) {
            return false;
        }
        
        String expression = (String) parameters.get("expression");
        if (expression == null || expression.trim().isEmpty()) {
            return false;
        }
        
        // Basic validation - check for safe mathematical expressions only
        return expression.matches("^[0-9+\\-*/.()\\s]+$");
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
        metadata.put("type", "calculator");
        metadata.put("supportedOperations", Arrays.asList("+", "-", "*", "/"));
        return metadata;
    }
    
    @Override
    public boolean isReady() {
        return isInitialized;
    }
    
    @Override
    public void shutdown() {
        isInitialized = false;
        logger.info("CalculatorTool shutdown complete");
    }
    
    /**
     * Simple expression evaluator for basic arithmetic.
     */
    private double evaluateSimpleExpression(String expression) throws Exception {
        // Clean the expression
        String cleaned = expression.replaceAll("\\s+", "");
        
        // Handle simple patterns like "72 * 24"
        Pattern multiplyPattern = Pattern.compile("(\\d+(?:\\.\\d+)?)\\s*\\*\\s*(\\d+(?:\\.\\d+)?)");
        Matcher matcher = multiplyPattern.matcher(cleaned);
        if (matcher.matches()) {
            double a = Double.parseDouble(matcher.group(1));
            double b = Double.parseDouble(matcher.group(2));
            return a * b;
        }
        
        // Handle addition
        Pattern addPattern = Pattern.compile("(\\d+(?:\\.\\d+)?)\\s*\\+\\s*(\\d+(?:\\.\\d+)?)");
        matcher = addPattern.matcher(cleaned);
        if (matcher.matches()) {
            double a = Double.parseDouble(matcher.group(1));
            double b = Double.parseDouble(matcher.group(2));
            return a + b;
        }
        
        // Handle subtraction
        Pattern subPattern = Pattern.compile("(\\d+(?:\\.\\d+)?)\\s*-\\s*(\\d+(?:\\.\\d+)?)");
        matcher = subPattern.matcher(cleaned);
        if (matcher.matches()) {
            double a = Double.parseDouble(matcher.group(1));
            double b = Double.parseDouble(matcher.group(2));
            return a - b;
        }
        
        // Handle division
        Pattern divPattern = Pattern.compile("(\\d+(?:\\.\\d+)?)\\s*/\\s*(\\d+(?:\\.\\d+)?)");
        matcher = divPattern.matcher(cleaned);
        if (matcher.matches()) {
            double a = Double.parseDouble(matcher.group(1));
            double b = Double.parseDouble(matcher.group(2));
            if (b == 0) throw new ArithmeticException("Division by zero");
            return a / b;
        }
        
        // Try to parse as single number
        try {
            return Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            throw new Exception("Unsupported expression format: " + expression);
        }
    }
}