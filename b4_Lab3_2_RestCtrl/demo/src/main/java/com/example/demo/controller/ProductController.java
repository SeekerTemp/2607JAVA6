package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService service;

    /*@Autowired
    //cach cu
    private ProductRepository productRepository;*/

    @GetMapping
    @ResponseBody
    public String view() {
        List<Product> productList = service.getAllProduct();

        StringBuilder sb = new StringBuilder();
        sb.append("<h1>List of products </h1>");
        for(Product product : productList) {
            sb.append("<p>"+product.getName()+"</p>");
        }
        return sb.toString();
    }
}
