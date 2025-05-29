package com.recipAI.server.domain.chat.dto;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;

@Getter
public class IngredientsRequest {

    @Value("${spring.ai.openai.chat.options.model}")
    private String OPENAI_MODEL;
    private final List<Message> messages;

    public IngredientsRequest(String imageUrl) {
        Message message = new Message(
                "user",
                List.of(
                        new Content("text", "첨부한 이미지에 보이는 재료들을 JSON 배열 형식의 리스트로만 반환해줘.  \n" +
                                "설명이나 문장 없이, 예를 들어 [\"사과\", \"연어\", \"브로콜리\"] 형식으로 응답해줘."),
                        new Content("image_url", Map.of("url", imageUrl))
                )
        );
        this.messages = List.of(message);
    }

    public record Message(String role, List<Content> content) {}

    @Getter
    public static class Content {
        private final String type;
        private String text;
        private Map<String, String> image_url;

        // 텍스트용 생성자
        public Content(String type, String text) {
            this.type = type;
            this.text = text;
        }

        // 이미지용 생성자
        public Content(String type, Map<String, String> image_url) {
            this.type = type;
            this.image_url = image_url;
        }

    }
}
