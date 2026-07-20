package com.example.demo.config;

import com.example.demo.model.Score;
import com.example.demo.repository.ScoreRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// Nạp điểm mẫu cho sinh viên id 1,2,3 khi khởi động
@Component
public class ScoreSeeder implements CommandLineRunner {

    private final ScoreRepository scoreRepository;

    public ScoreSeeder(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    @Override
    public void run(String... args) {
        if (scoreRepository.count() > 0) {
            return;
        }
        scoreRepository.save(new Score(null, 1L, "Java", 8.5));
        scoreRepository.save(new Score(null, 1L, "Spring Boot", 9.0));
        scoreRepository.save(new Score(null, 2L, "Java", 7.0));
        scoreRepository.save(new Score(null, 2L, "HTML/CSS", 8.0));
        scoreRepository.save(new Score(null, 3L, "Java", 6.5));
    }
}
