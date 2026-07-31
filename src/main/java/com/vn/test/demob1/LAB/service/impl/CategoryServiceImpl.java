package com.vn.test.demob1.LAB.service.impl;

import com.vn.test.demob1.LAB.model.Category;
import com.vn.test.demob1.LAB.repository.CategoryRepository;
import com.vn.test.demob1.LAB.repository.ProductRepository;
import com.vn.test.demob1.LAB.service.CategoryService;
import com.vn.test.demob1.LAB.util.ConflictException;
import com.vn.test.demob1.LAB.util.NotFoundException;
import com.vn.test.demob1.LAB.util.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository repository;

    @Autowired
    ProductRepository productRepository;

    @Override
    public List<Category> findAll() {
        return repository.findAll();
    }

    @Override
    public Category findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy loại hàng có mã " + id));
    }

    @Override
    public Category create(Category category) {
        validate(category);
        // Thêm mới: mã đã tồn tại thì báo lỗi thay vì ghi đè bản ghi cũ
        if (repository.existsById(category.getId())) {
            throw new ConflictException("Mã loại hàng " + category.getId() + " đã tồn tại!");
        }
        return repository.save(category);
    }

    @Override
    public Category update(String id, Category category) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Không tìm thấy loại hàng có mã " + id);
        }
        // Mã truyền vào mới là mã được sửa, tránh việc form đổi mã lại tạo thêm dòng mới
        category.setId(id);
        validate(category);
        return repository.save(category);
    }

    @Override
    public void deleteById(String id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Không tìm thấy loại hàng có mã " + id);
        }
        // Khóa ngoại: còn sản phẩm thuộc loại này thì không cho xóa
        long used = productRepository.countByCategoryId(id);
        if (used > 0) {
            throw new ConflictException("Không thể xóa: còn " + used + " sản phẩm thuộc loại hàng này!");
        }
        repository.deleteById(id);
    }

    private void validate(Category category) {
        if (category.getId() == null || category.getId().isBlank()) {
            throw new ValidationException("Mã loại hàng không được bỏ trống!");
        }
        if (category.getName() == null || category.getName().isBlank()) {
            throw new ValidationException("Tên loại hàng không được bỏ trống!");
        }
    }
}
