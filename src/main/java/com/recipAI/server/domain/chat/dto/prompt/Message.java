package com.recipAI.server.domain.chat.dto.prompt;

import java.util.List;

public record Message(String role, List<Content> content) {
}
