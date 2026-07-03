package com.vn.test.demob1.LAB.dao;

import com.vn.test.demob1.LAB.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDAO extends JpaRepository<Category, String> {
}
