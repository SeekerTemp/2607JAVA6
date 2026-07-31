package com.vn.test.demob1.LAB.service;

import com.vn.test.demob1.LAB.model.AppUser;

import java.util.List;

/**
 * LAB 7 - Bài 3: nghiệp vụ quản lý người sử dụng.
 */
public interface AppUserService {
    List<AppUser> findAll();

    AppUser findById(String username);

    AppUser create(AppUser user);

    AppUser update(String username, AppUser user);

    void deleteById(String username);
}
