package com.example.demo.dto;

// Điểm của sinh viên - dữ liệu lấy từ project Quản lý điểm (cổng 8082)
public class ScoreDto {
    private Long id;
    private Long studentId;
    private String subject;
    private Double score;

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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
