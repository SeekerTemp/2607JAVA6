package com.vn.test.demob1.LAB.service;

import com.vn.test.demob1.LAB.model.User;

import java.util.List;

public interface UserService {

    /**
     * Kiểm tra tài khoản/mật khẩu. Sai thông tin -> ném IllegalArgumentException.
     */
    User authenticate(String username, String password);

    List<User> findAll();
}
