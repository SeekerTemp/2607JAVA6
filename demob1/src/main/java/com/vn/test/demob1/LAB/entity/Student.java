package com.vn.test.demob1.LAB.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LAB 5 - Bài 4: lớp thực thể ánh xạ bảng J6Students.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "J6Students")
public class Student {
    @Id
    String id;
    String name;
    boolean gender;
    double mark;
}
