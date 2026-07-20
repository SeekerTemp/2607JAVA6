package com.example.demo.config;

import com.example.demo.model.Course;
import com.example.demo.model.Enrollment;
import com.example.demo.model.Student;
import com.example.demo.model.User;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.EnrollmentRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// Nạp dữ liệu mẫu cho H2 (in-memory) khi khởi động
@Component
public class DataSeeder implements CommandLineRunner {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;

    public DataSeeder(CourseRepository courseRepository,
                      StudentRepository studentRepository,
                      EnrollmentRepository enrollmentRepository,
                      UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        if (courseRepository.count() > 0) {
            return;
        }

        Course java = courseRepository.save(new Course(null, "Java 6", "Lập trình Java nâng cao", "Nguyen Van A", null));
        Course spring = courseRepository.save(new Course(null, "Spring Boot", "REST API với Spring Boot", "Tran Thi B", null));
        Course html = courseRepository.save(new Course(null, "HTML/CSS", "Thiết kế web cơ bản", "Le Van C", null));

        Student an = studentRepository.save(new Student(null, "Nguyen Van An", "an@gmail.com", null));
        Student binh = studentRepository.save(new Student(null, "Tran Van Binh", "binh@gmail.com", null));
        Student chi = studentRepository.save(new Student(null, "Le Thi Chi", "chi@gmail.com", null));

        enrollmentRepository.save(new Enrollment(null, an.getId(), java.getId(), null));
        enrollmentRepository.save(new Enrollment(null, an.getId(), spring.getId(), null));
        enrollmentRepository.save(new Enrollment(null, binh.getId(), java.getId(), null));
        enrollmentRepository.save(new Enrollment(null, chi.getId(), html.getId(), null));

        userRepository.save(new User(null, "Nguyen Van An", "an@gmail.com"));
        userRepository.save(new User(null, "Tran Van Binh", "binh@gmail.com"));
        userRepository.save(new User(null, "Le Thi Chi", "chi@gmail.com"));
    }
}
