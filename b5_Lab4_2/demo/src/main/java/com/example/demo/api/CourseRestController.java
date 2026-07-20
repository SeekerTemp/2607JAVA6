package com.example.demo.api;

import com.example.demo.model.Course;
import com.example.demo.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CourseRestController {
    @Autowired
    CourseRepository courseRepository;

    @GetMapping("/api/courses")
    public Object getAllCourses() {
        return courseRepository.findAll();
    }
    @GetMapping("/api/courses/{id}")
    public Object getCoursesById(@PathVariable Long id) {
        return courseRepository.findById(id);
    }

    @PostMapping("/api/courses")
    public Object newCourse(@RequestBody Course course) {
        return courseRepository.save(course);
    }

    @PutMapping("/api/courses/{id}")
    public Object updateCourse(@PathVariable Long id, @RequestBody Course course) {
        return courseRepository.save(course);
    }

    @DeleteMapping("/api/courses/{id}")
    public void updateCourse(@PathVariable Long id) {
        courseRepository.deleteById(id);
    }


}
