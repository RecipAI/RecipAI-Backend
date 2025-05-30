package com.recipAI.server.common.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GptResponseParser {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String extractMessageContent(String rawJson) {
        try {
            JsonNode root = objectMapper.readTree(rawJson);
            return root
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract content from GPT response", e);
        }
    }

}
