package com.vn.test.demob1.LAB.service;

import com.vn.test.demob1.LAB.model.Product;

import java.util.List;

/**
 * LAB 7 - Bài 2: nghiệp vụ quản lý sản phẩm.
 */
public interface ProductService {
    List<Product> findAll();

    Product findById(String id);

    Product create(Product product);

    Product update(String id, Product product);

    void deleteById(String id);
}
