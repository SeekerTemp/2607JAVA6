package com.example.demo.api;

import com.example.demo.model.Student;
import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class StudentRestController {
    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/api/student")
    public Object xemDanhSach(){
        return studentRepository.findAll();
    }
    @GetMapping("/api/student/{id}")
    public Object xemChiTiet(@PathVariable Long id){
        return studentRepository.findById(id).orElse(null);
    }

    @PostMapping("/api/student")
    public Object themMoi(@RequestBody Student student){
//        System.out.println(student.toString());
        return studentRepository.save(student);
    }

    @PutMapping("/api/student/{id}")
    public Object suaTheoId(@PathVariable Long id,@RequestBody Student student){
        student.setId(id);
        return studentRepository.save(student);
    }

    @DeleteMapping("/api/student/{id}")
    public void xoaTheoId(@PathVariable Long id){
        studentRepository.deleteById(id);
    }
}
