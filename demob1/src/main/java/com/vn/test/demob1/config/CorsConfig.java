package com.vn.test.demob1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Mo cho moi origin: l8-fe chay o cong 5173, nhung cham bai co the goi
        // tu Postman, tu may khac hoac tu cong khac -> khong gioi han origin.
        // Khong bat allowCredentials vi API nay khong dung cookie/session.
        registry.addMapping("/api/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
