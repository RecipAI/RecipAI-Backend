package com.recipAI.server.domain.chat.dto;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@ToString
@Getter
public class IngredientsRequest {
    private final String model;
    private final List<Message> messages;

    public IngredientsRequest(String model, List<String> imageUrls) {
        this.model = model;
        List<Content> contents = new ArrayList<>();
        contents.add(new Content("text", "첨부한 이미지에 보이는 재료들을 JSON 배열 형식의 리스트로만 반환해줘.  \n" +
                "설명이나 문장 없이, 예를 들어 [\"사과\", \"연어\", \"브로콜리\"] 형식으로 응답해줘."));
        for (String imageUrl : imageUrls) {
            log.info("[IngredientsRequest] imageUrl = {}", imageUrl);
            contents.add(new Content("image_url", Map.of("url", imageUrl)));
        }
        this.messages = List.of(new Message("user", contents));
    }

    public record Message(String role, List<Content> content) {
    }

    @ToString
    @Getter
    public static class Content {
        private final String type;
        private String text;
        private Map<String, String> image_url;

        public Content(String type, String text) {
            this.type = type;
            this.text = text;
        }

        public Content(String type, Map<String, String> image_url) {
            this.type = type;
            this.image_url = image_url;
        }
    }
}
