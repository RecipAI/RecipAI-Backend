package com.recipAI.server.common.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

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

    public static List<String> parseIngredients(String gptResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(gptResponse);
            String content = root.path("choices").get(0).path("message").path("content").asText();
            return mapper.readValue(content, List.class);
        } catch (Exception e) {
            throw new RuntimeException("GPT 응답 파싱 실패: " + gptResponse, e);
        }
    }

    public static boolean extractBooleanResponse(String rawJson) {
        try {
            JsonNode root = objectMapper.readTree(rawJson);

            String content = root
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText()
                    .trim()
                    .toLowerCase();

            if ("true".equals(content)) {
                return true;
            } else if ("false".equals(content)) {
                return false;
            } else {
                throw new RuntimeException("Unexpected GPT response: " + content);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract boolean from GPT response", e);
        }
    }

}





