package com.recipAI.server.domain.chat.dto.prompt;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
@Getter
public class Content {
    private final String type;
    private String text;
    private ImageUrl image_url;

    public Content(String type, String text) {
        this.type = type;
        this.text = text;
    }

    public Content(String type, ImageUrl image_url) {
        this.type = type;
        this.image_url = image_url;
    }

    public record ImageUrl(String url) {}
}
