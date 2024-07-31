package com.example.bechef.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // CORS: 교차 요청 안됨 원칙
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")    // 모든 주소 적용됨
                .allowedOrigins("Http://localhost:3000") // 테스트용 도메인
                .allowedOrigins("https://icy-water-02a4fb800.5.azurestaticapps.net") // 배포용 도메인
                .allowedMethods("GET", "POST", "PUT", "DELETE","OPTIONS") // HTTP request 메서드 허용
                .allowedHeaders("*") // request의 모든 header 허용
                .exposedHeaders("Authorization") // 클라이언트가 authorization은 볼수있게 해줌
                .allowCredentials(true); // 클라이언트 관련 메소드
    }
}
