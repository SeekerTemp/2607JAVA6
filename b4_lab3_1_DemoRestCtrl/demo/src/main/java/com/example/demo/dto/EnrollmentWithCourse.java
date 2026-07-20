package com.example.demo.dto;

import com.example.demo.model.Course;
import com.example.demo.model.Enrollment;

import java.time.LocalDateTime;

// Lượt đăng ký kèm thông tin khóa học
public class EnrollmentWithCourse {
    private Long id;
    private Long studentId;
    private Long courseId;
    private LocalDateTime enrolledAt;
    private Course course;

    public EnrollmentWithCourse() {
    }

    public EnrollmentWithCourse(Enrollment enrollment, Course course) {
        this.id = enrollment.getId();
        this.studentId = enrollment.getStudentId();
        this.courseId = enrollment.getCourseId();
        this.enrolledAt = enrollment.getEnrolledAt();
        this.course = course;
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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
