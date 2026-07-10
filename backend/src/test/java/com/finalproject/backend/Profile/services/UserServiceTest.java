package com.finalproject.backend.Profile.services;

import com.finalproject.backend.entities.User;
import com.finalproject.backend.profile.DTO.ProfileDTO;
import com.finalproject.backend.profile.UserService;
import com.finalproject.backend.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Should return ProfileDTO when user exists")
    void getUserSuccess() {
        User user = new User();
        user.setId(1);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@example.com");
        user.setAboutMe("Hello");
        user.setImagePath("/path.png");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        ProfileDTO result = userService.getUser(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john@example.com", result.getEmail());
        assertEquals("Hello", result.getAboutMe());
        assertEquals("/path.png", result.getImagePath());
    }

    @Test
    @DisplayName("Should return null when user does not exist")
    void getUserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        ProfileDTO result = userService.getUser(1);

        assertNull(result);
    }

    @Test
    @DisplayName("Should update user details successfully")
    void updatePublicSuccess() {
        User user = new User();
        user.setId(1);

        ProfileDTO dto = new ProfileDTO(1, "Jane", "Smith", "jane@example.com", "Bio", "/img.png");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        userService.UpdatePublicUser(dto);

        assertEquals("Jane", user.getFirstName());
        assertEquals("Smith", user.getLastName());
        assertEquals("Bio", user.getAboutMe());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should throw 404 response status exception when updating non-existent user")
    void updatePublicNotFound() {
        ProfileDTO dto = new ProfileDTO(1, "Jane", "Smith", "jane@example.com", "Bio", "/img.png");

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                userService.UpdatePublicUser(dto)
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should update profile picture path successfully")
    void updatePictureSuccess() {
        User user = new User();
        user.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        userService.updateProfilePicture(1, "/new-path.png");

        assertEquals("/new-path.png", user.getImagePath());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should throw exception when updating picture for non-existent user")
    void updatePictureNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
                userService.updateProfilePicture(1, "/new-path.png")
        );
    }

    @Test
    @DisplayName("Should return user id when email exists")
    void getIdByEmailSuccess() {
        User user = new User();
        user.setId(5);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        Integer id = userService.getIdByEmail("test@example.com");

        assertEquals(5, id);
    }

    @Test
    @DisplayName("Should throw 404 response status exception when email does not exist")
    void getIdByEmailNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                userService.getIdByEmail("test@example.com")
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("Email not Found", ex.getReason());
    }

    @Test
    @DisplayName("Should return full name when user exists")
    void getNameSuccess() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        String fullName = userService.getName(1);

        assertEquals("John Doe", fullName);
        verify(userRepository, times(2)).findById(1);
    }

    @Test
    @DisplayName("Should return string with null references when user does not exist")
    void getNameNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        String fullName = userService.getName(1);

        assertEquals("null null", fullName);
        verify(userRepository, times(2)).findById(1);
    }
}