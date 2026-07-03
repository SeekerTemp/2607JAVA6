package com.vn.test.demob1.LAB.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LAB 7 - Bài 1: loại hàng {id, name}
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "Categories")
public class Category {
    @Id
    String id;
    String name;
}
