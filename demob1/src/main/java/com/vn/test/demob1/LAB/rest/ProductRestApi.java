package com.vn.test.demob1.LAB.rest;

import com.vn.test.demob1.LAB.dao.ProductDAO;
import com.vn.test.demob1.LAB.entity.Product;
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
 * LAB 7 - Bài 2: REST API quản lý sản phẩm.
 */
@CrossOrigin("*")
@RestController
public class ProductRestApi {

    @Autowired
    ProductDAO dao;

    @GetMapping("products")
    public List<Product> findAll() {
        return dao.findAll();
    }

    @GetMapping("products/{id}")
    public Product findById(@PathVariable("id") String id) {
        return dao.findById(id).orElse(null);
    }

    @PostMapping("products")
    public Product create(@RequestBody Product product) {
        return dao.save(product);
    }

    @PutMapping("products/{id}")
    public Product update(@PathVariable("id") String id, @RequestBody Product product) {
        return dao.save(product);
    }

    @DeleteMapping("products/{id}")
    public void delete(@PathVariable("id") String id) {
        dao.deleteById(id);
    }
}
