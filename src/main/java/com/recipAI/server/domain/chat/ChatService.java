package com.recipAI.server.domain.chat;

import com.recipAI.server.domain.chat.dto.IngredientsRequest;
import com.recipAI.server.domain.chat.dto.IngredientsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatService {
    private final RestClient restClient;

    public IngredientsResponse requestIngredients(List<String> imageUrls) {
        IngredientsRequest request = new IngredientsRequest(imageUrls);
        String GptResponse = callGptWithImage(request);
        return new IngredientsResponse(GptResponse);
    }

    private String callGptWithImage(IngredientsRequest requestBody) {
        return restClient.post()
                .body(requestBody)
                .retrieve()
                .body(String.class);
    }

}
