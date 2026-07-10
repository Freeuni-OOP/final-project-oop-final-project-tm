package com.finalproject.backend.Repositories;

import com.finalproject.backend.entities.User;
import com.finalproject.backend.repositories.UserModuleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserModuleRepositoryTest {

    @Autowired
    private UserModuleRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void deleteUser_ShouldRemoveFromDatabase() {
        User user = new User();
        user = entityManager.persist(user);

        userRepository.delete(user);
        Optional<User> foundUser = userRepository.findById(user.getId());

        assertThat(foundUser).isEmpty();
    }
}