package com.vn.test.demob1.LAB.repository;

import com.vn.test.demob1.LAB.model.Product;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * LAB 7 - Bài 2: kho dữ liệu sản phẩm, lưu ngay trong bộ nhớ (không dùng CSDL).
 */
@Repository
public class ProductRepository {

    private final Map<String, Product> data = new LinkedHashMap<>();

    public ProductRepository() {
        // Dữ liệu mẫu, categoryId trỏ tới loại hàng trong CategoryRepository
        save(new Product("P01", "iPhone 15", 25000000, LocalDate.of(2026, 1, 10), "C01"));
        save(new Product("P02", "Galaxy S24", 20000000, LocalDate.of(2026, 2, 15), "C01"));
        save(new Product("P03", "MacBook Air M3", 32000000, LocalDate.of(2026, 3, 1), "C02"));
        save(new Product("P04", "Chuột Logitech", 500000, LocalDate.of(2026, 3, 20), "C03"));
    }

    public synchronized List<Product> findAll() {
        return new ArrayList<>(data.values());
    }

    public synchronized Optional<Product> findById(String id) {
        return Optional.ofNullable(data.get(id));
    }

    public synchronized boolean existsById(String id) {
        return data.containsKey(id);
    }

    public synchronized Product save(Product product) {
        data.put(product.getId(), product);
        return product;
    }

    public synchronized void deleteById(String id) {
        data.remove(id);
    }

    public synchronized void deleteAll() {
        data.clear();
    }

    public synchronized long count() {
        return data.size();
    }

    /**
     * Đếm số sản phẩm thuộc một loại hàng, dùng để chặn xóa loại hàng còn được tham chiếu.
     */
    public synchronized long countByCategoryId(String categoryId) {
        return data.values().stream()
                .filter(p -> p.getCategoryId() != null && p.getCategoryId().equals(categoryId))
                .count();
    }
}
