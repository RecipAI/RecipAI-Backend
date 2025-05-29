package com.recipAI.server.domain.chat.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IngredientsResponse {
    private final List<String> ingredients;

    public IngredientsResponse(String gptResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            this.ingredients = objectMapper.readValue(gptResponse, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            throw new RuntimeException("GPT 응답 파싱 실패: " + gptResponse, e);
        }
    }
}
