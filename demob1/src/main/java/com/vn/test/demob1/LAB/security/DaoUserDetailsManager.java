package com.vn.test.demob1.LAB.security;

import com.vn.test.demob1.LAB.dao.UserDAO;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Nguồn dữ liệu người dùng tùy biến đọc từ CSDL J6Security2
 * qua UserDAO (JpaRepository).
 *
 * Fix so với slide: slide dùng {@code @Autowired UserDAO dao} rồi
 * {@code new DaoUserDetailsManager()} nên field không được inject.
 * Ở đây dùng constructor injection và được nối dây trong SecurityConfig.
 */
public class DaoUserDetailsManager implements UserDetailsService {

    private final UserDAO dao;

    public DaoUserDetailsManager(UserDAO dao) {
        this.dao = dao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.vn.test.demob1.LAB.entity.User user = dao.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));

        String password = user.getPassword();

        String[] roles = user.getUserRoles().stream()
                .map(ur -> ur.getRole().getId().substring(5)) // ROLE_USER -> USER
                .toArray(String[]::new);

        return User.withUsername(username)
                .password(password)
                .roles(roles)
                .build();
    }
}
