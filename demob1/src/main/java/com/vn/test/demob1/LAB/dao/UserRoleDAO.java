package com.vn.test.demob1.LAB.dao;

import com.vn.test.demob1.LAB.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleDAO extends JpaRepository<UserRole, Long> {
}
