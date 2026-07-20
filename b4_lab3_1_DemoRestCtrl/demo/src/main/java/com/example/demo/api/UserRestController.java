package com.example.demo.api;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// API users - được project cổng 8002 gọi sang lấy dữ liệu
@RestController
@RequestMapping("/api/users")
public class UserRestController {
    @Autowired
    private UserRepository userRepository;

    // Lấy danh sách users
    @GetMapping
    public List<User> getAll() {
        return userRepository.findAll();
    }

    // Lấy user theo mã
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public User create(@RequestBody User user) {
        user.setId(null);
        return userRepository.save(user);
    }
}
