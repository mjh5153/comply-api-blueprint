package com.init_spring_bean_mvn.demo.lpa;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.http.HttpResponse;

public class JsonBodyHandler implements HttpResponse.BodyHandler<JsonNode> { // hierachy type that impelements josn in java
    // disadvantage - private constructor can limit extending this class
    private final ObjectMapper objectMapper;

    private JsonBodyHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public static JsonBodyHandler create(ObjectMapper objectMapper) {
        return new JsonBodyHandler(objectMapper);
    }

    @Override
    public HttpResponse.BodySubscriber<JsonNode> apply(HttpResponse.ResponseInfo response) {
        return HttpResponse.BodySubscribers.mapping(HttpResponse.BodySubscribers.ofByteArray(), byteArray -> {
            try {
                return objectMapper.readTree(byteArray);

            } catch (IOException io) {
                throw new RuntimeException("Failed to pass data");
            }
        });
    }
}
