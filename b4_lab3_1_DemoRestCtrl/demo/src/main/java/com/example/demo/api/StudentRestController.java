package com.example.demo.api;

import com.example.demo.dto.EnrollmentWithCourse;
import com.example.demo.model.Course;
import com.example.demo.model.Enrollment;
import com.example.demo.model.Student;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.EnrollmentRepository;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentRestController {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private CourseRepository courseRepository;

    // Lấy danh sách học viên
    @GetMapping
    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    // Lấy học viên theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Student> getById(@PathVariable Long id) {
        return studentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Tạo học viên mới
    @PostMapping
    public Student create(@RequestBody Student student) {
        student.setId(null);
        return studentRepository.save(student);
    }

    // Cập nhật học viên theo ID
    @PutMapping("/{id}")
    public Student update(@PathVariable Long id, @RequestBody Student student) {
        student.setId(id);
        return studentRepository.save(student);
    }

    // Xóa học viên theo ID
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        studentRepository.deleteById(id);
    }

    // Danh sách khóa học mà một học viên đã đăng ký
    @GetMapping("/{studentId}/courses")
    public List<Course> coursesOfStudent(@PathVariable Long studentId) {
        List<Course> courses = new ArrayList<>();
        for (Enrollment e : enrollmentRepository.findByStudentId(studentId)) {
            courseRepository.findById(e.getCourseId()).ifPresent(courses::add);
        }
        return courses;
    }

    // Tất cả lượt đăng ký của một học viên (kèm thông tin khóa học)
    @GetMapping("/{studentId}/enrollments")
    public List<EnrollmentWithCourse> enrollmentsOfStudent(@PathVariable Long studentId) {
        List<EnrollmentWithCourse> result = new ArrayList<>();
        for (Enrollment e : enrollmentRepository.findByStudentId(studentId)) {
            Course course = courseRepository.findById(e.getCourseId()).orElse(null);
            result.add(new EnrollmentWithCourse(e, course));
        }
        return result;
    }
}
