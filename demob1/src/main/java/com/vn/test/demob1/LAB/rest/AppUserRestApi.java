package com.vn.test.demob1.LAB.rest;

import com.vn.test.demob1.LAB.dao.AppUserDAO;
import com.vn.test.demob1.LAB.entity.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * LAB 7 - Bài 3: REST API quản lý người sử dụng.
 */
@CrossOrigin("*")
@RestController
public class AppUserRestApi {

    @Autowired
    AppUserDAO dao;

    @GetMapping("accounts")
    public List<AppUser> findAll() {
        return dao.findAll();
    }

    @GetMapping("accounts/{username}")
    public AppUser findById(@PathVariable("username") String username) {
        return dao.findById(username).orElse(null);
    }

    @PostMapping("accounts")
    public AppUser create(@RequestBody AppUser user) {
        return dao.save(user);
    }

    @PutMapping("accounts/{username}")
    public AppUser update(@PathVariable("username") String username, @RequestBody AppUser user) {
        return dao.save(user);
    }

    @DeleteMapping("accounts/{username}")
    public void delete(@PathVariable("username") String username) {
        dao.deleteById(username);
    }
}
