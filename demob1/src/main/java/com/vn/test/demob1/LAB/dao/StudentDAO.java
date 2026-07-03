package com.vn.test.demob1.LAB.dao;

import com.vn.test.demob1.LAB.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentDAO extends JpaRepository<Student, String> {
}
