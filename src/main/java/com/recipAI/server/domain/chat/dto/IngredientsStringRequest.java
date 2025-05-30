package com.recipAI.server.domain.chat.dto;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IngredientsStringRequest {
    private List<String> ingredients;
}
