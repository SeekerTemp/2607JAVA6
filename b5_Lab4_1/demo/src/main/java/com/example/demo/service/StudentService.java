package com.example.demo.service;

import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository repository;
    public StudentService(StudentRepository studentRepository) {this.repository = studentRepository; }

    public List<Student> getAll(){
        return repository.findAll();
    }

    public Student getById(Long id){
        return repository.findById(id).get();
    }

    public Student addNewStudent(Student student){
        student.setId(null);
        return repository.save(student);
    }

}