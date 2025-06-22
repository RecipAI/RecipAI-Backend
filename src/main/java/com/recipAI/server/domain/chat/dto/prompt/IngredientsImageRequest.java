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
        contents.add(new Content("text", "첨부한 이미지에 보이는 식재료들을 JSON 배열 형식의 리스트로만 반환해줘.\n" +
                "설명이나 문장 없이, 예를 들어 [\"사과\", \"연어\", \"브로콜리\"] 형식으로 응답해줘.\n" +
                "모든 재료를 나열하지 말고, 인식된 재료 중 상위 80% 정도의 중요도나 빈도 기준으로 핵심 재료만 포함해줘.\n " +
                "만약 식재료가 하나도 없으면 빈 배열로 결과 반환해줘."));
        log.info("[IngredientsRequest] imageUrl = {}", imageUrl);
        contents.add(new Content("image_url", new ImageUrl(imageUrl)));
        this.messages = List.of(new Message("user", contents));
    }
}