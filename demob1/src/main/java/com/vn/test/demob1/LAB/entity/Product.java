package com.vn.test.demob1.LAB.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * LAB 7 - Bài 2: sản phẩm {id, name, price, date, categoryId}
 * categoryId là khóa ngoại tham chiếu đến Category.id
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "Products")
public class Product {
    @Id
    String id;
    String name;
    double price;
    LocalDate date;
    String categoryId;
}
