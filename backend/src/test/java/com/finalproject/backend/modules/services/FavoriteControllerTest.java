package com.finalproject.backend.modules.services;

import com.finalproject.backend.entities.Service;
import com.finalproject.backend.entities.User;
import com.finalproject.backend.repositories.FavoriteRepository;
import com.finalproject.backend.repositories.UserModuleRepository;
import com.finalproject.backend.repositories.listingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FavoriteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private UserModuleRepository userRepository;

    @Mock
    private listingRepository listingRepository;

    @InjectMocks
    private FavoriteController favoriteController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(favoriteController).build();
    }

    @Test
    void toggleFavorite_RemoveWhenExists() throws Exception {
        when(favoriteRepository.existsByUserIdAndServiceId(1, 1)).thenReturn(true);

        mockMvc.perform(post("/api/favorites/1/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Removed"));

        verify(favoriteRepository).deleteByUserIdAndServiceId(1, 1);
    }

    @Test
    void toggleFavorite_AddWhenNotExists() throws Exception {
        when(favoriteRepository.existsByUserIdAndServiceId(1, 1)).thenReturn(false);
        when(userRepository.findById(1)).thenReturn(Optional.of(new User()));
        when(listingRepository.findById(1)).thenReturn(Optional.of(new Service()));

        mockMvc.perform(post("/api/favorites/1/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Added"));

        verify(favoriteRepository).save(any(Favorite.class));
    }

    @Test
    void getUserFavorites_ReturnsList() throws Exception {
        Favorite fav = new Favorite();
        fav.setService(new Service());
        when(favoriteRepository.findAllByUserId(1)).thenReturn(List.of(fav));

        mockMvc.perform(get("/api/favorites/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void checkFavorite_ReturnsBoolean() throws Exception {
        when(favoriteRepository.existsByUserIdAndServiceId(1, 1)).thenReturn(true);

        mockMvc.perform(get("/api/favorites/check/1/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}