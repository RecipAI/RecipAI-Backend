package com.recipAI.server.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipAI.server.common.exception.RecipAIException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import static com.recipAI.server.common.response.BaseResponseStatus.JSON_PARSING_FAIL;

@Slf4j
public class Serializer {
    static ObjectMapper objectMapper = new ObjectMapper();

    public static String serializeList(List<String> stringList) {
        try {
            return objectMapper.writeValueAsString(stringList);
        } catch (JsonProcessingException e) {
            throw new RecipAIException(JSON_PARSING_FAIL);
        }
    }

    public static String serializeObject(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RecipAIException(JSON_PARSING_FAIL);
        }
    }

    public static Map<String, List<String>> deserializeToMap(String rawJson) {
        log.info("[deserializeToMap] rawJson= {}", rawJson);
        String json = extractJsonContent(rawJson);
        log.info("[deserializeToMap] json= {}", json);
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, new TypeReference<Map<String, List<String>>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid JSON format", e);
        }
    }

    public static String extractJsonContent(String rawContent) {
        // GPT 응답이 ```json\n{...}\n``` 형태로 감싸져 있을 때 이를 제거
        if (rawContent.startsWith("```json")) {
            return rawContent.replaceAll("(?s)```json\\s*", "").replaceAll("```", "").trim();
        }
        return rawContent.trim();
    }
}
