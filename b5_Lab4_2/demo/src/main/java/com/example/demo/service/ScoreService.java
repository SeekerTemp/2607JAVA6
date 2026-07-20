package com.example.demo.service;

import com.example.demo.dto.StudentAverageDto;
import com.example.demo.model.Score;
import com.example.demo.repository.ScoreRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScoreService {

    private final ScoreRepository scoreRepository;
    private final StudentClientService studentClientService;

    public ScoreService(ScoreRepository scoreRepository, StudentClientService studentClientService) {
        this.scoreRepository = scoreRepository;
        this.studentClientService = studentClientService;
    }

    // Danh sách điểm của một sinh viên
    public List<Score> getByStudent(Long studentId) {
        return scoreRepository.findByStudentId(studentId);
    }

    // Thêm điểm cho sinh viên
    public Score addScore(Long studentId, Score score) {
        score.setId(null);
        score.setStudentId(studentId);
        return scoreRepository.save(score);
    }

    // Danh sách gồm tên sinh viên và điểm trung bình
    // (tên sinh viên lấy từ project cổng 8081)
    public List<StudentAverageDto> getAverageByStudent() {
        // Gom điểm theo studentId
        Map<Long, List<Score>> grouped = new LinkedHashMap<>();
        for (Score s : scoreRepository.findAll()) {
            grouped.computeIfAbsent(s.getStudentId(), k -> new ArrayList<>()).add(s);
        }

        List<StudentAverageDto> result = new ArrayList<>();
        for (Map.Entry<Long, List<Score>> entry : grouped.entrySet()) {
            Long studentId = entry.getKey();
            List<Score> scores = entry.getValue();

            double sum = 0;
            for (Score s : scores) {
                sum += s.getScore() == null ? 0 : s.getScore();
            }
            double average = scores.isEmpty() ? 0 : sum / scores.size();

            String name = studentClientService.getStudentName(studentId);
            result.add(new StudentAverageDto(studentId, name, average));
        }
        return result;
    }
}
