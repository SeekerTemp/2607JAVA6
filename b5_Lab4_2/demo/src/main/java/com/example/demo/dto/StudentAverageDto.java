package com.example.demo.dto;

// Tên sinh viên và điểm trung bình
public class StudentAverageDto {
    private Long studentId;
    private String studentName;
    private Double averageScore;

    public StudentAverageDto() {
    }

    public StudentAverageDto(Long studentId, String studentName, Double averageScore) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.averageScore = averageScore;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }
}
