package com.vn.test.demob1.LAB.service;

import com.vn.test.demob1.LAB.entity.Student;

import java.util.List;

public interface StudentService {
    List<Student> findAll();

    Student findById(String id);

    Student create(Student student);

    Student update(Student student);

    void deleteById(String id);
}
