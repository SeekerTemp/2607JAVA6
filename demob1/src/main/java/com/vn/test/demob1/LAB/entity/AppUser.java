package com.vn.test.demob1.LAB.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LAB 7 - Bài 3: người sử dụng {username, password, fullname, enabled, role}
 * role thuộc {"USER", "ADMIN"}
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "Accounts")
public class AppUser {
    @Id
    String username;
    String password;
    String fullname;
    boolean enabled;
    String role;
}
