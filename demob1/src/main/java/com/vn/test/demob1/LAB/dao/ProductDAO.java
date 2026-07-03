package com.vn.test.demob1.LAB.dao;

import com.vn.test.demob1.LAB.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDAO extends JpaRepository<Product, String> {
}
