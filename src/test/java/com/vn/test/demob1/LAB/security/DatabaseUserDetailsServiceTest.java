package com.vn.test.demob1.LAB.security;

import com.vn.test.demob1.LAB.model.AppUser;
import com.vn.test.demob1.LAB.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DatabaseUserDetailsServiceTest {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AppUserRepository repository;

    @Test
    void shouldLoadUserFromDatabase() {
        AppUser user = new AppUser("db.user@gmail.com", "123", "DB User", true, "USER");
        repository.save(user);

        UserDetails loaded = userDetailsService.loadUserByUsername("db.user@gmail.com");

        assertThat(loaded.getUsername()).isEqualTo("db.user@gmail.com");
        assertThat(loaded.getAuthorities()).extracting(authority -> authority.getAuthority())
                .contains("ROLE_USER");
    }
}
