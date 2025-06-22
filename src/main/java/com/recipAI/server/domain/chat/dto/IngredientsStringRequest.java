package com.recipAI.server.domain.chat.dto;

import com.recipAI.server.common.utils.Serializer;
import com.recipAI.server.domain.chat.dto.prompt.Content;
import com.recipAI.server.domain.chat.dto.prompt.Message;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ToString
@Getter
public class IngredientsStringRequest {
    private final String model;
    private final List<Message> messages;

    public IngredientsStringRequest(String model, List<String> ingredients) {
        this.model = model;
        List<Content> contents = new ArrayList<>();
        contents.add(new Content("text", "해당 식재료 목록에 "));
        log.info("[IngredientsStringRequest] ingredients = {}", ingredients);
        contents.add(new Content("text", Serializer.serializeList(ingredients)));
        this.messages = List.of(new Message("user", contents));
    }
}
