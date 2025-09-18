package com.comply.ai.agents;

import java.util.List;
import java.util.Map;

/**
 * Describes the capabilities and parameters of an agent tool.
 */
public class ToolCapabilities {
    private final String description;
    private final List<String> requiredParameters;
    private final List<String> optionalParameters;
    private final Map<String, String> parameterDescriptions;
    private final String returnType;
    
    public ToolCapabilities(String description, List<String> requiredParameters, 
                           List<String> optionalParameters, Map<String, String> parameterDescriptions,
                           String returnType) {
        this.description = description;
        this.requiredParameters = requiredParameters;
        this.optionalParameters = optionalParameters;
        this.parameterDescriptions = parameterDescriptions;
        this.returnType = returnType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public List<String> getRequiredParameters() {
        return requiredParameters;
    }
    
    public List<String> getOptionalParameters() {
        return optionalParameters;
    }
    
    public Map<String, String> getParameterDescriptions() {
        return parameterDescriptions;
    }
    
    public String getReturnType() {
        return returnType;
    }
    
    @Override
    public String toString() {
        return "ToolCapabilities{" +
                "description='" + description + '\'' +
                ", requiredParameters=" + requiredParameters +
                ", optionalParameters=" + optionalParameters +
                ", returnType='" + returnType + '\'' +
                '}';
    }
}