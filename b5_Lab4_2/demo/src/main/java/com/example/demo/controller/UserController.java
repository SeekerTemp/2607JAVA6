package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.model.MockApiUser;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
/*
    @GetMapping
    public List<MockApiUser> getAllUsers() {
        List<MockApiUser>users=userService.getAllUsers();
        return users;
    }*/
    @GetMapping
    public List<UserDto> getAllUsers() {
        /*return userService.getAllUsers().stream()
                .map(user -> {
                    UserDto userDto = new UserDto();
                    userDto.setName(user.getName());
                    userDto.setAvatar(user.getAvatar());
                    return userDto;
                })
                .toList();*/

        List<MockApiUser> mockApiUsers = userService.getAllUsers();
        List<UserDto> userDtos = new ArrayList<>();
        for (MockApiUser mockApiUser : mockApiUsers) {
            UserDto userDto = new UserDto();
            userDto.setName(mockApiUser.getName());
            userDto.setAvatar(mockApiUser.getAvatar());
            userDtos.add(userDto);

        }
        return userDtos;
    }
}
