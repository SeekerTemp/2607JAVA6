package com.example.demo.api;

import com.example.demo.model.Enrollment;
import com.example.demo.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class EnrollmentRestController {
    @Autowired
    EnrollmentRepository courseRepository;

    @GetMapping("/api/enrollment")
    public Object getAllEnrollments() {
        return courseRepository.findAll();
    }
    @GetMapping("/api/enrollment/{id}")
    public Object getEnrollmentsById(@PathVariable Long id) {
        return courseRepository.findById(id);
    }

    @PostMapping("/api/enrollment")
    public Object newEnrollment(@RequestBody Enrollment course) {
        return courseRepository.save(course);
    }

    @PutMapping("/api/enrollment/{id}")
    public Object updateEnrollment(@PathVariable Long id, @RequestBody Enrollment course) {
        return courseRepository.save(course);
    }

    @DeleteMapping("/api/enrollment/{id}")
    public void updateEnrollment(@PathVariable Long id) {
        courseRepository.deleteById(id);
    }


}
