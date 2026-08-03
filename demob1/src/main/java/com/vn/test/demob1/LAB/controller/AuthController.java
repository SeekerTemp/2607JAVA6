package com.vn.test.demob1.LAB.controller;

import com.vn.test.demob1.LAB.model.User;
import com.vn.test.demob1.LAB.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * LAB 8: đăng nhập cho panel 2 và panel 3 (panel 1 là kênh chung, không cần đăng nhập).
 */
@RestController
@RequestMapping("/api")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> body) {
        try {
            User user = userService.authenticate(body.get("username"), body.get("password"));
            return ResponseEntity.ok(info(user));
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new LinkedHashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    /**
     * Danh sách tài khoản để panel chọn kênh chat riêng.
     */
    @GetMapping("/users")
    public List<Map<String, String>> users() {
        List<Map<String, String>> result = new ArrayList<>();
        for (User user : userService.findAll()) {
            result.add(info(user));
        }
        return result;
    }

    private Map<String, String> info(User user) {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("username", user.getUsername());
        data.put("displayName", user.getDisplayName());
        return data;
    }
}
