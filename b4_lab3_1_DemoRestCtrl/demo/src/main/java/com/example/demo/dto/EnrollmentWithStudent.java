package com.example.demo.dto;

import com.example.demo.model.Enrollment;
import com.example.demo.model.Student;

import java.time.LocalDateTime;

// Lượt đăng ký kèm thông tin học viên
public class EnrollmentWithStudent {
    private Long id;
    private Long studentId;
    private Long courseId;
    private LocalDateTime enrolledAt;
    private Student student;

    public EnrollmentWithStudent() {
    }

    public EnrollmentWithStudent(Enrollment enrollment, Student student) {
        this.id = enrollment.getId();
        this.studentId = enrollment.getStudentId();
        this.courseId = enrollment.getCourseId();
        this.enrolledAt = enrollment.getEnrolledAt();
        this.student = student;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public LocalDateTime getEnrolledAt() {
        return enrolledAt;
    }

    public void setEnrolledAt(LocalDateTime enrolledAt) {
        this.enrolledAt = enrolledAt;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
