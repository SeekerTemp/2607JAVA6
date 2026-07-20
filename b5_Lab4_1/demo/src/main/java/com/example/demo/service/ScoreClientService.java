package com.example.demo.service;

import com.example.demo.dto.ScoreDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

// Gọi sang project Quản lý điểm (cổng 8082) để lấy điểm của sinh viên
@Service
public class ScoreClientService {

    private final RestTemplate restTemplate;

    @Value("${score-service.base-url:http://localhost:8082}")
    private String scoreServiceBaseUrl;

    public ScoreClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ScoreDto> getScoresByStudent(Long studentId) {
        String url = scoreServiceBaseUrl + "/api/score/" + studentId;
        ScoreDto[] scores = restTemplate.getForObject(url, ScoreDto[].class);
        return scores == null ? List.of() : Arrays.asList(scores);
    }
}
