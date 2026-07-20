package com.example.demo.service;

import com.example.demo.model.MockApiUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private RestTemplate restTemplate;

    public List<MockApiUser> getAllUsers() {
        String url = "https://67414054e4647499008d305b.mockapi.io/api/v1/users";
        MockApiUser[] users = restTemplate.getForObject(url, MockApiUser[].class);
        return Arrays.asList(users);
    }

    public MockApiUser getUserById(Long id) {
        String url = "https://67414054e4647499008d305b.mockapi.io/api/v1/users"+id;
        return restTemplate.getForObject(url, MockApiUser.class);
    }

}
