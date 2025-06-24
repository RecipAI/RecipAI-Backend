package com.recipAI.server.domain.chat.dto.prompt;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.recipAI.server.domain.chat.dto.prompt.Content.*;

@Slf4j
@ToString
@Getter
public class IngredientsImageRequest {
    private final String model;
    private final List<Message> messages;

    public IngredientsImageRequest(String model, String imageUrl) {
        this.model = model;
        List<Content> contents = new ArrayList<>();
        String prompt = "나는 이미지를 분석해 식재료를 인식하는 AI인데, 정확도는 약 80% 수준이야.\n" +
                "첨부된 이미지에서 인식한 식재료들을 JSON 배열 형식으로만 반환할게.\n" +
                "설명이나 문장 없이, 예를 들어 [\"사과\", \"연어\", \"브로콜리\"]처럼 응답해줘.\n" +
                "모든 재료를 나열하지 말고, 인식된 재료 중 상위 약 80% 중요도 또는 빈도 기준으로 핵심 재료만 포함해줘.\n" +
                "만약 확실하게 인식된 식재료가 하나도 없으면, 빈 배열 []만 반환해줘.";
        contents.add(new Content("text", prompt));
        log.info("[IngredientsRequest] imageUrl = {}", imageUrl);
        contents.add(new Content("image_url", new ImageUrl(imageUrl)));
        this.messages = List.of(new Message("user", contents));
    }
}