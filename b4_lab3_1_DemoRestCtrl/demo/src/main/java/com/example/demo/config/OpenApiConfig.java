package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Cấu hình Swagger / OpenAPI - truy cập tại /swagger-ui.html
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI().info(new Info()
                .title("Lab 3.1 - Course / Student / Enrollment API")
                .description("RESTful API cho quản lý khóa học, học viên và đăng ký học")
                .version("v1"));
    }
}
