package com.vn.test.demob1.LAB.service;

import com.vn.test.demob1.LAB.model.Category;

import java.util.List;

/**
 * LAB 7 - Bài 1: nghiệp vụ quản lý loại hàng.
 */
public interface CategoryService {
    List<Category> findAll();

    Category findById(String id);

    Category create(Category category);

    Category update(String id, Category category);

    void deleteById(String id);
}
