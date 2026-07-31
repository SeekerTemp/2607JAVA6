package com.vn.test.demob1.LAB;

import com.vn.test.demob1.LAB.model.AppUser;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AppUserRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldPersistAndLoadAppUserEntity() {
        AppUser user = new AppUser("new.user@gmail.com", "123", "New User", true, "USER");

        entityManager.persist(user);
        entityManager.flush();
        entityManager.clear();

        AppUser loaded = entityManager.find(AppUser.class, user.getUsername());

        assertThat(loaded).isNotNull();
        assertThat(loaded.getFullname()).isEqualTo("New User");
    }
}
