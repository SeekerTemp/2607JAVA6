package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

// Gọi sang project Quản lý sinh viên (cổng 8081) để lấy tên sinh viên
@Service
public class StudentClientService {

    private final RestTemplate restTemplate;

    @Value("${student-service.base-url:http://localhost:8081}")
    private String studentServiceBaseUrl;

    public StudentClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // GET 8081/api/students/{id} trả về tên sinh viên (chuỗi)
    public String getStudentName(Long studentId) {
        try {
            String url = studentServiceBaseUrl + "/api/students/" + studentId;
            return restTemplate.getForObject(url, String.class);
        } catch (Exception ex) {
            return "Unknown";
        }
    }
}
