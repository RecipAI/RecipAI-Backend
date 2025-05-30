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
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

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
