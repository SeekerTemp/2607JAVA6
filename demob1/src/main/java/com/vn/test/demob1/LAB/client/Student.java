package com.vn.test.demob1.LAB.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LAB 5 - Bài 2: mô tả cấu trúc dữ liệu JSON của sinh viên (dùng cho Swing client).
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Student {
    private String id;
    private String name;
    private double mark;
    private boolean gender;
}
