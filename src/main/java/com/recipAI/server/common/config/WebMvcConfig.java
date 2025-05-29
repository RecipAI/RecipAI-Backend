package com.recipAI.server.common.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${spring.ai.openai.api-key}")
    private String OPENAI_API_KEY;

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader(HttpHeaders.AUTHORIZATION, OPENAI_API_KEY)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("[addCorsMappings] CorsMapping 호출");
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")        // 추후에 프론트 도메인 추가하기
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .exposedHeaders("Authorization", "Set-Cookie")
                .allowCredentials(true)     // 쿠키 허용
                .maxAge(3000);              // 원하는 시간만큼 pre-flight 리퀘스트를 캐싱
    }
}
