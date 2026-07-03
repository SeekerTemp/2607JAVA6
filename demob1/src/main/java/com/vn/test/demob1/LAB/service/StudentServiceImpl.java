package com.vn.test.demob1.LAB.service;

import com.vn.test.demob1.LAB.dao.StudentDAO;
import com.vn.test.demob1.LAB.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    StudentDAO dao;

    @Override
    public List<Student> findAll() {
        return dao.findAll();
    }

    @Override
    public Student findById(String id) {
        return dao.findById(id).get();
    }

    @Override
    public Student create(Student student) {
        return dao.save(student);
    }

    @Override
    public Student update(Student student) {
        return dao.save(student);
    }

    @Override
    public void deleteById(String id) {
        dao.deleteById(id);
    }
}
