package com.finalproject.backend.Repositories;

import com.finalproject.backend.entities.Service;
import com.finalproject.backend.entities.User;
import com.finalproject.backend.modules.services.Favorite;
import com.finalproject.backend.repositories.FavoriteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FavoriteRepositoryTest {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User user;
    private Service service;

    @BeforeEach
    void setUp() {
        user = new User();
        entityManager.persist(user);
        service = new Service();
        entityManager.persist(service);
    }

    @Test
    void testFindAllByUserId() {
        Favorite fav = new Favorite();
        fav.setUser(user);
        fav.setService(service);
        entityManager.persist(fav);

        List<Favorite> favorites = favoriteRepository.findAllByUserId(user.getId());

        assertEquals(1, favorites.size());
        assertEquals(user.getId(), favorites.get(0).getUser().getId());
    }

    @Test
    void testExistsByUserIdAndServiceId() {
        Favorite fav = new Favorite();
        fav.setUser(user);
        fav.setService(service);
        entityManager.persist(fav);

        boolean exists = favoriteRepository.existsByUserIdAndServiceId(user.getId(), service.getId());
        boolean notExists = favoriteRepository.existsByUserIdAndServiceId(user.getId(), 999);

        assertTrue(exists);
        assertFalse(notExists);
    }

    @Test
    void testDeleteByUserIdAndServiceId() {
        Favorite fav = new Favorite();
        fav.setUser(user);
        fav.setService(service);
        entityManager.persist(fav);

        favoriteRepository.deleteByUserIdAndServiceId(user.getId(), service.getId());
        entityManager.flush();

        assertFalse(favoriteRepository.existsByUserIdAndServiceId(user.getId(), service.getId()));
    }
}