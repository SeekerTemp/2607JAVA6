package com.vn.test.demob1.LAB.controller;

import com.vn.test.demob1.LAB.model.Category;
import com.vn.test.demob1.LAB.service.CategoryService;
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
 * LAB 7 - Bài 1: REST API quản lý loại hàng {id, name}.
 */
@CrossOrigin("*")
@RestController
@RequestMapping("categories")
public class CategoryController {

    @Autowired
    CategoryService service;

    @GetMapping
    public List<Category> findAll() {
        return service.findAll();
    }

    @GetMapping("{id}")
    public Category findById(@PathVariable("id") String id) {
        return service.findById(id);
    }

    @PostMapping
    public Category create(@RequestBody Category category) {
        return service.create(category);
    }

    @PutMapping("{id}")
    public Category update(@PathVariable("id") String id, @RequestBody Category category) {
        return service.update(id, category);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") String id) {
        service.deleteById(id);
    }
}
