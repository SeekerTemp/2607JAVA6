package com.vn.test.demob1.LAB.controller;

import com.vn.test.demob1.LAB.model.AppUser;
import com.vn.test.demob1.LAB.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * LAB 7 - Bài 3: REST API quản lý người sử dụng
 * {username, password, fullname, enabled, role} với role thuộc {"USER", "ADMIN"}.
 */
@CrossOrigin("*")
@RestController
@RequestMapping("accounts")
public class AppUserController {

    private final AppUserService service;

    @Autowired
    public AppUserController(AppUserService service) {
        this.service = service;
    }

    @GetMapping
    public List<AppUser> findAll() {
        return service.findAll();
    }

    @GetMapping("{username}")
    public AppUser findById(@PathVariable("username") String username) {
        return service.findById(username);
    }

    @PostMapping
    public AppUser create(@RequestBody AppUser user) {
        return service.create(user);
    }

    @PutMapping("{username}")
    public AppUser update(@PathVariable("username") String username, @RequestBody AppUser user) {
        return service.update(username, user);
    }

    @DeleteMapping("{username}")
    public void delete(@PathVariable("username") String username) {
        service.deleteById(username);
    }
}
