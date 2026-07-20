package com.example.demo.api;

import com.example.demo.dto.EnrollmentWithStudent;
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
@RequestMapping("/api/courses")
public class CourseRestController {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private StudentRepository studentRepository;

    // Lấy tất cả khóa học
    @GetMapping
    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    // Lấy một khóa học theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Course> getById(@PathVariable Long id) {
        return courseRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Tạo khóa học mới
    @PostMapping
    public Course create(@RequestBody Course course) {
        course.setId(null);
        return courseRepository.save(course);
    }

    // Cập nhật khóa học theo ID
    @PutMapping("/{id}")
    public Course update(@PathVariable Long id, @RequestBody Course course) {
        course.setId(id);
        return courseRepository.save(course);
    }

    // Xóa khóa học theo ID
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        courseRepository.deleteById(id);
    }

    // Danh sách học viên của một khóa học
    @GetMapping("/{courseId}/students")
    public List<Student> studentsOfCourse(@PathVariable Long courseId) {
        List<Student> students = new ArrayList<>();
        for (Enrollment e : enrollmentRepository.findByCourseId(courseId)) {
            studentRepository.findById(e.getStudentId()).ifPresent(students::add);
        }
        return students;
    }

    // Tất cả lượt đăng ký của một khóa học (kèm thông tin học viên)
    @GetMapping("/{courseId}/enrollments")
    public List<EnrollmentWithStudent> enrollmentsOfCourse(@PathVariable Long courseId) {
        List<EnrollmentWithStudent> result = new ArrayList<>();
        for (Enrollment e : enrollmentRepository.findByCourseId(courseId)) {
            Student student = studentRepository.findById(e.getStudentId()).orElse(null);
            result.add(new EnrollmentWithStudent(e, student));
        }
        return result;
    }
}
