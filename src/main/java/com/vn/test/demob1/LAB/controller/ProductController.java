package com.vn.test.demob1.LAB.controller;

import com.vn.test.demob1.LAB.model.Product;
import com.vn.test.demob1.LAB.service.ProductService;
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
 * LAB 7 - Bài 2: REST API quản lý sản phẩm {id, name, price, date, categoryId}.
 * categoryId là khóa ngoại tham chiếu đến loại hàng ở Bài 1.
 */
@CrossOrigin("*")
@RestController
@RequestMapping("products")
public class ProductController {

    @Autowired
    ProductService service;

    @GetMapping
    public List<Product> findAll() {
        return service.findAll();
    }

    @GetMapping("{id}")
    public Product findById(@PathVariable("id") String id) {
        return service.findById(id);
    }

    @PostMapping
    public Product create(@RequestBody Product product) {
        return service.create(product);
    }

    @PutMapping("{id}")
    public Product update(@PathVariable("id") String id, @RequestBody Product product) {
        return service.update(id, product);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") String id) {
        service.deleteById(id);
    }
}
