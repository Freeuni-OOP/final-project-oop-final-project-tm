package com.finalproject.backend.modules.services;

import com.finalproject.backend.entities.Service;
import com.finalproject.backend.entities.User;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FavoriteTest {

    @Test
    void testFavoriteFields() {
        Favorite favorite = new Favorite();
        User user = new User();
        Service service = new Service();
        LocalDateTime now = LocalDateTime.now();

        favorite.setId(1);
        favorite.setUser(user);
        favorite.setService(service);
        favorite.setCreation_time(now);

        assertEquals(1, favorite.getId());
        assertEquals(user, favorite.getUser());
        assertEquals(service, favorite.getService());
        assertEquals(now, favorite.getCreation_time());
    }

    @Test
    void testDefaultValues() {
        Favorite favorite = new Favorite();
        assertNotNull(favorite.getCreation_time());
    }
}