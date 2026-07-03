package com.vn.test.demob1.LAB.dao;

import com.vn.test.demob1.LAB.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<User, String> {
}
