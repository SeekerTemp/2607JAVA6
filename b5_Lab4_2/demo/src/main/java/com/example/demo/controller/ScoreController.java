package com.example.demo.controller;

import com.example.demo.dto.StudentAverageDto;
import com.example.demo.model.Score;
import com.example.demo.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/score")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    // Danh sách gồm tên sinh viên và điểm trung bình
    @GetMapping
    public List<StudentAverageDto> getAverages() {
        return scoreService.getAverageByStudent();
    }

    // Danh sách điểm của một sinh viên
    @GetMapping("/{studentId}")
    public List<Score> getByStudent(@PathVariable Long studentId) {
        return scoreService.getByStudent(studentId);
    }

    // Thêm điểm cho sinh viên
    @PostMapping("/{studentId}")
    public Score addScore(@PathVariable Long studentId, @RequestBody Score score) {
        return scoreService.addScore(studentId, score);
    }
}
