package com.vn.test.demob1.LAB.service.impl;

import com.vn.test.demob1.LAB.model.AppUser;
import com.vn.test.demob1.LAB.repository.AppUserRepository;
import com.vn.test.demob1.LAB.service.AppUserService;
import com.vn.test.demob1.LAB.util.ConflictException;
import com.vn.test.demob1.LAB.util.NotFoundException;
import com.vn.test.demob1.LAB.util.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppUserServiceImpl(AppUserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<AppUser> findAll() {
        return repository.findAll();
    }

    @Override
    public AppUser findById(String username) {
        return repository.findById(username)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng " + username));
    }

    @Override
    public AppUser create(AppUser user) {
        validate(user);
        if (repository.existsById(user.getUsername())) {
            throw new ConflictException("Tên đăng nhập " + user.getUsername() + " đã tồn tại!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    @Override
    public AppUser update(String username, AppUser user) {
        if (!repository.existsById(username)) {
            throw new NotFoundException("Không tìm thấy người dùng " + username);
        }
        user.setUsername(username);
        validate(user);
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return repository.save(user);
    }

    @Override
    public void deleteById(String username) {
        if (!repository.existsById(username)) {
            throw new NotFoundException("Không tìm thấy người dùng " + username);
        }
        repository.deleteById(username);
    }

    private void validate(AppUser user) {
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new ValidationException("Tên đăng nhập không được bỏ trống!");
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new ValidationException("Mật khẩu không được bỏ trống!");
        }
        if (user.getFullname() == null || user.getFullname().isBlank()) {
            throw new ValidationException("Họ và tên không được bỏ trống!");
        }
        if (!"USER".equals(user.getRole()) && !"ADMIN".equals(user.getRole())) {
            throw new ValidationException("Vai trò chỉ được là USER hoặc ADMIN!");
        }
    }
}
