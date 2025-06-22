package com.recipAI.server.domain.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipAI.server.common.exception.RecipAIException;
import com.recipAI.server.domain.chat.dto.IngredientsResponse;
import com.recipAI.server.domain.chat.dto.IngredientsStringRequest;
import com.recipAI.server.domain.chat.dto.MenusResponse;
import com.recipAI.server.domain.chat.dto.prompt.RecipeRequest;
import com.recipAI.server.domain.chat.dto.RecipeResponse;
import com.recipAI.server.domain.chat.dto.prompt.IngredientsImageRequest;
import com.recipAI.server.domain.chat.dto.prompt.MenusRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

import static com.recipAI.server.common.response.BaseResponseStatus.*;
import static com.recipAI.server.common.utils.GptResponseParser.*;
import static com.recipAI.server.common.utils.Serializer.serializeObject;

@Slf4j
@Service
public class ChatService {
    @Value("${openai.model}")
    private String OPENAI_MODEL;

    private final String OPENAI_API_KEY;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public ChatService(RestClient.Builder builder, ObjectMapper objectMapper, Environment environment) {
        this.OPENAI_API_KEY = environment.getProperty("openai.api-key");
        this.restClient = builder
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + OPENAI_API_KEY)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.objectMapper = objectMapper;
    }

    public IngredientsResponse requestIngredients(String imageUrl) {
        IngredientsImageRequest request = new IngredientsImageRequest(OPENAI_MODEL, imageUrl);
        log.info("[requestIngredients] IngredientsRequest = {}", request.toString());
        List<String> ingredients = parseIngredients(callGpt(request));
        log.info("[requestIngredients] ingredients = {}", ingredients.toString());
        return new IngredientsResponse(ingredients);
    }

    public Boolean validateIngredients(String imageUrl) {
        IngredientsImageRequest request = new IngredientsImageRequest(OPENAI_MODEL, imageUrl);
        log.info("[validateIngredients] IngredientsImageRequest = {}", request.toString());
        return extractBooleanResponse(callGpt(request));
    }


    public MenusResponse requestMenus(List<String> ingredients) {
        MenusRequest request = new MenusRequest(OPENAI_MODEL, ingredients);
        log.info("[requestMenus] MenusRequest = {}", request.toString());
        String gptResponse = extractMessageContent(callGpt(request));
        log.info("[requestMenus] GPT가 준 응답 = {}", gptResponse);
        return new MenusResponse(gptResponse);
    }


    public RecipeResponse requestRecipe(String menuName) {
        RecipeRequest request = new RecipeRequest(OPENAI_MODEL, menuName);
        log.info("[requestRecipe] RecipeRequest = {}", request.toString());
        String gptResponse = extractMessageContent(callGpt(request));
        log.info("[requestRecipe] GPT가 준 응답 = {}", gptResponse);
        if (gptResponse.trim().equalsIgnoreCase("UnrecognizedDishException")) {
            throw new RecipAIException(UNRECOGNIZED_DISH);
        }
        return new RecipeResponse(gptResponse);
    }


    private <T> String callGpt(T request) {
        log.info("[OpenAI Request] Body = {}", serializeObject(request));
        try {
            return restClient.post()
                    .body(request)
                    .retrieve()
                    .body(String.class);
        } catch (HttpClientErrorException e) {
            throw handleGptException(e);
        }
    }


    private RecipAIException handleGptException(HttpClientErrorException e) {
        String responseBody = e.getResponseBodyAsString();
        log.error("[handleGptException] response body = {}", responseBody);
        try {
            Map<String, Object> errorMap = objectMapper.readValue(responseBody, Map.class);
            Map<String, Object> errorDetails = (Map<String, Object>) errorMap.get("error");

            String type = (String) errorDetails.get("type");
            String code = (String) errorDetails.get("code");
            String message = (String) errorDetails.get("message");

            log.error("[handleGptException] type = {}", type);
            log.error("[handleGptException] code = {}", code);
            log.error("[handleGptException] message = {}", message);

            if ("invalid_request_error".equals(type)) {
                if ("context_length_exceeded".equals(code)) {
                    throw new RecipAIException(CONTEXT_LENGTH_EXCEEDED, message);
                }
                if ("model_not_found".equals(code)) {
                    throw new RecipAIException(INVALID_MODEL, message);
                }
                if ("invalid_api_key".equals(code)) {
                    throw new RecipAIException(INVALID_API_KEY, message);
                }
                if (message != null && message.contains("missing required parameter")) {
                    throw new RecipAIException(INVALID_REQUEST_ERROR, message);
                }
                throw new RecipAIException(INVALID_REQUEST_ERROR, message);
            }
            if ("authentication_error".equals(type)) {
                if ("invalid_api_key".equals(code)) {
                    throw new RecipAIException(INVALID_API_KEY, message);
                }
                if ("missing_api_key".equals(code)) {
                    throw new RecipAIException(MISSING_API_KEY, message);
                }
            }
            if ("rate_limit_error".equals(type)) {
                throw new RecipAIException(RATE_LIMIT_ERROR, message);
            }
            if ("insufficient_quota".equals(code)) {
                log.error("insufficient_quota");
                throw new RecipAIException(INSUFFICIENT_QUOTA, message);
            }
            log.warn("[OpenAI Error] type={}, code={}, message={}", type, code, message);
            throw new RecipAIException(OPENAI_API_ERROR, message);
        } catch (JsonProcessingException parsingException) {
            log.error("OpenAI 응답 파싱 실패", parsingException.getMessage());
            throw new RecipAIException(INVALID_REQUEST_ERROR);
        }
    }
}
