package com.vn.test.demob1.LAB.dao;

import com.vn.test.demob1.LAB.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDAO extends JpaRepository<Role, String> {
}
