package com.vn.test.demob1.LAB.service.impl;

import com.vn.test.demob1.LAB.model.Product;
import com.vn.test.demob1.LAB.repository.CategoryRepository;
import com.vn.test.demob1.LAB.repository.ProductRepository;
import com.vn.test.demob1.LAB.service.ProductService;
import com.vn.test.demob1.LAB.util.ConflictException;
import com.vn.test.demob1.LAB.util.NotFoundException;
import com.vn.test.demob1.LAB.util.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository repository;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<Product> findAll() {
        return repository.findAll();
    }

    @Override
    public Product findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm có mã " + id));
    }

    @Override
    public Product create(Product product) {
        validate(product);
        // Thêm mới: mã đã tồn tại thì báo lỗi thay vì ghi đè bản ghi cũ
        if (repository.existsById(product.getId())) {
            throw new ConflictException("Mã sản phẩm " + product.getId() + " đã tồn tại!");
        }
        return repository.save(product);
    }

    @Override
    public Product update(String id, Product product) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Không tìm thấy sản phẩm có mã " + id);
        }
        // Mã truyền vào mới là mã được sửa, tránh việc form đổi mã lại tạo thêm dòng mới
        product.setId(id);
        validate(product);
        return repository.save(product);
    }

    @Override
    public void deleteById(String id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Không tìm thấy sản phẩm có mã " + id);
        }
        repository.deleteById(id);
    }

    private void validate(Product product) {
        if (product.getId() == null || product.getId().isBlank()) {
            throw new ValidationException("Mã sản phẩm không được bỏ trống!");
        }
        if (product.getName() == null || product.getName().isBlank()) {
            throw new ValidationException("Tên sản phẩm không được bỏ trống!");
        }
        if (product.getPrice() <= 0) {
            throw new ValidationException("Giá sản phẩm phải lớn hơn 0!");
        }
        if (product.getDate() == null) {
            throw new ValidationException("Ngày nhập không được bỏ trống!");
        }
        // Kiểm tra khóa ngoại trước khi ghi để báo lỗi rõ ràng thay vì lỗi SQL
        if (product.getCategoryId() == null || product.getCategoryId().isBlank()) {
            throw new ValidationException("Phải chọn loại hàng cho sản phẩm!");
        }
        if (!categoryRepository.existsById(product.getCategoryId())) {
            throw new ValidationException("Loại hàng " + product.getCategoryId() + " không tồn tại!");
        }
    }
}
