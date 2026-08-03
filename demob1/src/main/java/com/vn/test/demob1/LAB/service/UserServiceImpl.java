package com.vn.test.demob1.LAB.service;

import com.vn.test.demob1.LAB.model.User;
import com.vn.test.demob1.LAB.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Sai tài khoản hoặc mật khẩu");
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
