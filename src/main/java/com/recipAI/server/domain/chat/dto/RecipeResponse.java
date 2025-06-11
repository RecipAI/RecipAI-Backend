package com.recipAI.server.domain.chat.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecipeResponse {
    private List<String> recipe;
    private List<String> youtube;

    public RecipeResponse(String gptResponse) {
        parseGptResponse(gptResponse);
    }

    private void parseGptResponse(String gptResponse) {
        try {
            String jsonPart = gptResponse.substring(gptResponse.indexOf("{"));

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonPart);

            this.recipe = new ArrayList<>();
            root.get("recipe").forEach(step -> this.recipe.add(step.asText()));

            this.youtube = new ArrayList<>();
            root.get("youtube").forEach(link -> this.youtube.add(link.asText()));
        } catch (Exception e) {
            log.error("응답 파싱 중 오류 발생", e);
            throw new RuntimeException("응답 파싱 중 오류 발생", e);
        }
    }
}