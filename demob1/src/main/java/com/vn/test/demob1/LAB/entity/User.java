package com.vn.test.demob1.LAB.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@Table(name = "J6users")
public class User {
    @Id
    String username;
    String password;
    boolean enabled;

    // Bỏ khỏi toString/equals để tránh đệ quy vô hạn (User <-> UserRole)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    List<UserRole> userRoles;
}
