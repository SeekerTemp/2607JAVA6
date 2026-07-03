package com.vn.test.demob1.LAB.rest;

import com.vn.test.demob1.LAB.dao.CategoryDAO;
import com.vn.test.demob1.LAB.entity.Category;
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
 * LAB 7 - Bài 1: REST API quản lý loại hàng.
 */
@CrossOrigin("*")
@RestController
public class CategoryRestApi {

    @Autowired
    CategoryDAO dao;

    @GetMapping("categories")
    public List<Category> findAll() {
        return dao.findAll();
    }

    @GetMapping("categories/{id}")
    public Category findById(@PathVariable("id") String id) {
        return dao.findById(id).orElse(null);
    }

    @PostMapping("categories")
    public Category create(@RequestBody Category category) {
        return dao.save(category);
    }

    @PutMapping("categories/{id}")
    public Category update(@PathVariable("id") String id, @RequestBody Category category) {
        return dao.save(category);
    }

    @DeleteMapping("categories/{id}")
    public void delete(@PathVariable("id") String id) {
        dao.deleteById(id);
    }
}
