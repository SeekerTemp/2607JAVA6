package com.example.demo.config;

import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// Nạp danh sách sinh viên mẫu (id 1,2,3) khi khởi động
@Component
public class StudentSeeder implements CommandLineRunner {

    private final StudentRepository studentRepository;

    public StudentSeeder(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void run(String... args) {
        if (studentRepository.count() > 0) {
            return;
        }
        Student s1 = new Student();
        s1.setName("Nguyen Van An");
        studentRepository.save(s1);

        Student s2 = new Student();
        s2.setName("Tran Van Binh");
        studentRepository.save(s2);

        Student s3 = new Student();
        s3.setName("Le Thi Chi");
        studentRepository.save(s3);
    }
}
