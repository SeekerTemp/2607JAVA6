package com.example.demo.service;

import com.example.demo.dto.RemoteUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

// Gọi sang project cổng 8001 để lấy dữ liệu users
@Service
public class UserService {

    private final RestTemplate restTemplate;

    // Địa chỉ project 8001 (Lab 3.1)
    @Value("${lab31.base-url:http://localhost:8001}")
    private String baseUrl;

    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Lấy danh sách users từ 8001
    public List<RemoteUser> getAllUsers() {
        String url = baseUrl + "/api/users";
        RemoteUser[] users = restTemplate.getForObject(url, RemoteUser[].class);
        return users == null ? List.of() : Arrays.asList(users);
    }

    // Lấy user theo mã từ 8001
    public RemoteUser getUserById(Long id) {
        String url = baseUrl + "/api/users/" + id;
        return restTemplate.getForObject(url, RemoteUser.class);
    }
}
