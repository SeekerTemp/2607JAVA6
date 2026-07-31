package com.vn.test.demob1.LAB.repository;

import com.vn.test.demob1.LAB.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * LAB 7 - Bài 3: kho dữ liệu người sử dụng dùng Spring Data JPA.
 */
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, String> {
}
