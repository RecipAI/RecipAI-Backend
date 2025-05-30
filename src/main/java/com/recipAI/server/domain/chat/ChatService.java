package com.recipAI.server.domain.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipAI.server.common.exception.RecipAIException;
import com.recipAI.server.domain.chat.dto.IngredientsRequest;
import com.recipAI.server.domain.chat.dto.IngredientsResponse;
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

@Slf4j
@Service
public class ChatService {
    @Value("${spring.ai.openai.chat.options.model}")
    private String OPENAI_MODEL;

    private final String OPENAI_API_KEY;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public ChatService(RestClient.Builder builder, ObjectMapper objectMapper, Environment environment) {
        this.OPENAI_API_KEY = environment.getProperty("spring.ai.openai.api-key");
        this.restClient = builder
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + OPENAI_API_KEY)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.objectMapper = objectMapper;
    }

    public IngredientsResponse requestIngredients(List<String> imageUrls) {
        IngredientsRequest request = new IngredientsRequest(OPENAI_MODEL, imageUrls);
        log.info("[requestIngredients] IngredientsRequest = {}", request.toString());
        String GptResponse = callGptWithImage(request);
        return new IngredientsResponse(GptResponse);
    }

    private String callGptWithImage(IngredientsRequest requestBody) {
        String jsonBody = null;
        try {
            jsonBody = objectMapper.writeValueAsString(requestBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        log.info("[OpenAI Request] Body = {}", jsonBody);


        try {
            return restClient.post()
                    .body(requestBody)
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
