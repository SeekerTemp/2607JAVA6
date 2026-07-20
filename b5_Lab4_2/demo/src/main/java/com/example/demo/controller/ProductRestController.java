package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
public class ProductRestController {

    @Autowired
    private ProductService service;

    @GetMapping
    public Object getAll(){
        return service.getAllProduct();
    }

    @GetMapping("/{id}")
    public Object getById(@PathVariable Long id){
        return service.getById(id);
    }

    @PostMapping
    public Object add(@RequestBody Product product){
        return service.addNewProduct(product);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable Long id, @RequestBody Product product){
        return service.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable Long id){
        return service.deleteById(id);
    }

}
