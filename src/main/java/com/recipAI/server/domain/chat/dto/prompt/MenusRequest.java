package com.recipAI.server.domain.chat.dto.prompt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipAI.server.common.exception.RecipAIException;
import com.recipAI.server.common.utils.Serializer;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.recipAI.server.common.response.BaseResponseStatus.JSON_PARSING_FAIL;

@Slf4j
@ToString
@Getter
public class MenusRequest {
    private final String model;
    private final List<Message> messages;

    public MenusRequest(String model, List<String> ingredients) {
        this.model = model;
        List<Content> contents = new ArrayList<>();
        String prompt = "다음은 재료 목록이야: " + String.join(", ", ingredients) +
                ". 이 재료들을 바탕으로 만들 수 있는 요리 메뉴를 5개 추천해줘. " +
                "각 요리는 key로, 필요한 재료 목록은 value로 구성된 JSON 객체 형태로만 응답해줘. " +
                "설명이나 문장 없이 순수 JSON으로만 출력해줘. 예: {\"김치찌개\": [\"김치\", \"돼지고기\"], \"계란찜\": [\"계란\", \"소금\"]}";
        contents.add(new Content("text", prompt));
        contents.add(new Content("text", Serializer.serializeList(ingredients)));
        this.messages = List.of(new Message("user", contents));
    }

}
