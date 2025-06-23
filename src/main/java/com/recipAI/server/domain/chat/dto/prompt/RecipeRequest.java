package com.recipAI.server.domain.chat.dto.prompt;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ToString
@Getter
public class RecipeRequest {

    private final String model;
    private final List<Message> messages;

    public RecipeRequest(String model, String menuName) {
        this.model = model;
        List<Content> contents = new ArrayList<>();
        String prompt = "다음 문자열은 요리 메뉴 이름일 수도 있고 아닐 수도 있어: \"" + menuName + "\".\n" +
                "만약 요리 이름이 아니라면, \"UnrecognizedDishException\" 이라고만 응답해줘. 다른 설명은 절대 하지 마.\n" +
                "반대로 이 문자열이 실제 요리 메뉴 이름이라면, 해당 요리를 만드는 과정을 번호를 붙여 단계별로 설명해줘.\n" +
                "그리고 해당 요리법이 잘 설명된 유튜브 링크 2024년 이후 영상으로 3개도 함께 제공해줘.\n" +
                "최종 응답은 아래 형식의 순수 JSON 객체여야 해. 설명 없이 반드시 이 형식만 따르도록 해:\n" +
                "{\n" +
                "  \"recipe\": [\"1. 첫 번째 단계\", \"2. 두 번째 단계\", ...],\n" +
                "  \"youtube\": [\"유튜브 링크1\", \"유튜브 링크2\", \"유튜브 링크3\"]\n" +
                "}";
        contents.add(new Content("text", prompt));
        this.messages = List.of(new Message("user", contents));
    }
}
