package com.vn.test.demob1.LAB.repository;

import com.vn.test.demob1.LAB.model.Category;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * LAB 7 - Bài 1: kho dữ liệu loại hàng, lưu ngay trong bộ nhớ (không dùng CSDL).
 * LinkedHashMap để giữ đúng thứ tự thêm vào khi hiển thị.
 * Các phương thức đều synchronized vì nhiều request có thể chạy song song.
 */
@Repository
public class CategoryRepository {

    private final Map<String, Category> data = new LinkedHashMap<>();

    public CategoryRepository() {
        // Dữ liệu mẫu
        save(new Category("C01", "Điện thoại"));
        save(new Category("C02", "Laptop"));
        save(new Category("C03", "Phụ kiện"));
    }

    public synchronized List<Category> findAll() {
        return new ArrayList<>(data.values());
    }

    public synchronized Optional<Category> findById(String id) {
        return Optional.ofNullable(data.get(id));
    }

    public synchronized boolean existsById(String id) {
        return data.containsKey(id);
    }

    public synchronized Category save(Category category) {
        data.put(category.getId(), category);
        return category;
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
}
