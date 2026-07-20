package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    //DI bang constructor
    private final ProductRepository repository;
    public ProductService(ProductRepository productRepository) {
        this.repository = productRepository;
    }

    public List<Product> getAllProduct(){
        List<Product> productList = repository.findAll();
        productList.forEach(product -> {
            product.setName("PRO"+product.getName());
        });
        return productList;
    }

    public Product getById(Long id) {
        return repository.findById(id).get();
    }

    public Product updateProduct(Long id,Product updated) {
        updated.setId(id);
        return repository.save(updated);
    }

    public Product addNewProduct(Product newProduct) {
        //dam bao save ol
        newProduct.setId(null);
        return repository.save(newProduct);
    }

    public Product deleteById(Long id) {
        Product product = repository.findById(id).orElse(null);
        if (product != null) {
            repository.delete(product);
        }
        return product;
    }
}
