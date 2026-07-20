package com.example.demo.controller;

import com.example.demo.dto.ScoreDto;
import com.example.demo.model.Student;
import com.example.demo.service.ScoreClientService;
import com.example.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    @Autowired
    private StudentService service;
    @Autowired
    private ScoreClientService scoreClientService;

    // Danh sách sinh viên
    @GetMapping
    public Object findAll() {
        return service.getAll();
    }

    // Trả về tên sinh viên theo id
    @GetMapping("/{id}")
    public Object xemChiTiet(@PathVariable Long id) {
        Student student = service.getById(id);
        return student.getName();
    }

    // Thêm sinh viên
    @PostMapping
    public Object themMoi(@RequestBody Student student) {
        return service.addNewStudent(student);
    }

    // Trả về danh sách điểm của sinh viên (gọi sang project cổng 8082)
    @GetMapping("/{id}/score")
    public List<ScoreDto> getScores(@PathVariable Long id) {
        return scoreClientService.getScoresByStudent(id);
    }
}
