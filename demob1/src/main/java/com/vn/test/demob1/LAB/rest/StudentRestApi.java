package com.vn.test.demob1.LAB.rest;

import com.vn.test.demob1.LAB.entity.Student;
import com.vn.test.demob1.LAB.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * LAB 5 - Bài 4: REST API quản lý sinh viên làm việc với CSDL J6Students.
 */
@CrossOrigin("*")
@RestController
public class StudentRestApi {

    @Autowired
    StudentService studentService;

    @GetMapping("students") // => [student1, student2, ...]
    public List<Student> findAll() {
        return studentService.findAll();
    }

    @GetMapping("students/{id}") // => student
    public Student findById(@PathVariable("id") String id) {
        return studentService.findById(id);
    }

    @PostMapping("students") // => student
    public Student create(@RequestBody Student student) {
        return studentService.create(student);
    }

    @PutMapping("students/{id}") // => student
    public Student update(@PathVariable("id") String id, @RequestBody Student student) {
        return studentService.update(student);
    }

    @DeleteMapping("students/{id}") // => nothing
    public void delete(@PathVariable("id") String id) {
        studentService.deleteById(id);
    }
}
