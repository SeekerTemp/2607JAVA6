package com.vn.test.demob1.LAB.dao;

import com.vn.test.demob1.LAB.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserDAO extends JpaRepository<AppUser, String> {
}
