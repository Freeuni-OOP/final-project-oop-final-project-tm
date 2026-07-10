package com.finalproject.backend.Repositories;

import com.finalproject.backend.entities.Service;
import com.finalproject.backend.entities.User;
import com.finalproject.backend.modules.services.Favorite;
import com.finalproject.backend.repositories.listingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class listingRepositoryTest {

    @Autowired
    private listingRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private Service s1, s2;
    private User u1;

    @BeforeEach
    void setUp() {
        u1 = new User();
        entityManager.persist(u1);

        s1 = new Service();
        s1.setTitle("Cleaning Service");
        s1.setCategory("Cleaning");
        s1.setPrice(50.0);
        entityManager.persist(s1);

        s2 = new Service();
        s2.setTitle("Gardening Service");
        s2.setCategory("Gardening");
        s2.setPrice(150.0);
        entityManager.persist(s2);
    }

    @Test
    void findByFilters_NoFilters_ReturnsAll() {
        List<Service> result = repository.findByFilters(null, null, null, null, null, null, Sort.by("id"));
        assertThat(result).hasSize(2);
    }

    @Test
    void findByFilters_TextAndCategoryFilter_ReturnsMatch() {
        List<Service> result = repository.findByFilters("Clean", "Cleaning", null, null, null, null, Sort.by("id"));
        assertThat(result).containsExactly(s1);
    }

    @Test
    void findByFilters_PriceRange_ReturnsMatch() {
        List<Service> result = repository.findByFilters(null, null, 100.0, 200.0, null, null, Sort.by("id"));
        assertThat(result).containsExactly(s2);
    }

    @Test
    void findByFilters_FavoriteFilter_ReturnsMatch() {
        Favorite fav = new Favorite();
        fav.setService(s1);
        fav.setUser(u1);
        entityManager.persist(fav);

        List<Service> result = repository.findByFilters(null, null, null, null, (long) u1.getId(), null, Sort.by("id"));
        assertThat(result).containsExactly(s1);
    }

    @Test
    void findByFilters_ExcludeFavoriteFilter_ReturnsMatch() {
        Favorite fav = new Favorite();
        fav.setService(s1);
        fav.setUser(u1);
        entityManager.persist(fav);

        List<Service> result = repository.findByFilters(null, null, null, null, null, (long) u1.getId(), Sort.by("id"));
        assertThat(result).containsExactly(s2);
    }
}