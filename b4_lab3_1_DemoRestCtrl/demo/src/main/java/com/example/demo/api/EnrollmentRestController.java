package com.example.demo.api;

import com.example.demo.model.Enrollment;
import com.example.demo.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentRestController {
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    // Lấy tất cả lượt đăng ký
    @GetMapping
    public List<Enrollment> getAll() {
        return enrollmentRepository.findAll();
    }

    // Lấy chi tiết đăng ký theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Enrollment> getById(@PathVariable Long id) {
        return enrollmentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Đăng ký học viên vào khóa học
    @PostMapping
    public Enrollment create(@RequestBody Enrollment enrollment) {
        enrollment.setId(null);
        return enrollmentRepository.save(enrollment);
    }

    // Hủy đăng ký học theo ID
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        enrollmentRepository.deleteById(id);
    }
}
