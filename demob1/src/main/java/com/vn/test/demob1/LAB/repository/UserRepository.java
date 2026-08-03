package com.vn.test.demob1.LAB.repository;

import com.vn.test.demob1.LAB.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * LAB 8: kho tài khoản trong bộ nhớ (không dùng CSDL), seed dữ liệu mẫu ở constructor.
 */
@Repository
public class UserRepository {

    private final Map<String, User> users = new LinkedHashMap<>();

    public UserRepository() {
        save(new User("u2", "123", "Thị Nở"));
        save(new User("u3", "123", "Chí Phèo"));
    }

    private void save(User user) {
        users.put(user.getUsername(), user);
    }

    public User findByUsername(String username) {
        return username == null ? null : users.get(username);
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
}
