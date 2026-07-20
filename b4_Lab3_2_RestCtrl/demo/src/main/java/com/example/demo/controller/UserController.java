package com.example.demo.controller;

import com.example.demo.dto.RemoteUser;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Từ project 8002 gọi sang 8001 lấy dữ liệu users
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    // Lấy danh sách users (dữ liệu từ 8001)
    @GetMapping
    public List<RemoteUser> getAllUsers() {
        return userService.getAllUsers();
    }

    // Lấy user theo mã (dữ liệu từ 8001)
    @GetMapping("/{id}")
    public RemoteUser getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}
