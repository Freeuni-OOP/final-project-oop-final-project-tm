package com.finalproject.backend.Profile.controllers;

import com.finalproject.backend.login_register.config.TokenCreator;
import com.finalproject.backend.profile.PictureController;
import com.finalproject.backend.profile.PictureService;
import com.finalproject.backend.profile.UserService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PictureController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                UserDetailsServiceAutoConfiguration.class
        })
class PictureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PictureService pictureService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private TokenCreator tokenCreator;

    @Test
    void uploadImage_ShouldReturnSavedPath_WhenCookieIsValid() throws Exception {
        String mockToken = "valid-jwt";
        String mockEmail = "elene@example.com";
        int mockUserId = 42;
        String savedFileName = "uploads/profile_42.png";

        MockMultipartFile mockFile = new MockMultipartFile(
                "picUrl",
                "avatar.png",
                MediaType.IMAGE_PNG_VALUE,
                "fake-image-bytes".getBytes()
        );

        when(pictureService.saveImage(any())).thenReturn(savedFileName);
        when(tokenCreator.validateTokenAndGetEmail(mockToken)).thenReturn(mockEmail);
        when(userService.getIdByEmail(mockEmail)).thenReturn(mockUserId);

        mockMvc.perform(multipart("/api/images")
                        .file(mockFile)
                        .cookie(new Cookie("jwt_token", mockToken)))
                .andExpect(status().isOk())
                .andExpect(content().string(savedFileName));

        verify(pictureService, times(1)).saveImage(mockFile);
        verify(userService, times(1)).updateProfilePicture(mockUserId, savedFileName);
    }

    @Test
    void getImage_ShouldReturnJpegResource_WhenExtensionIsJpg() throws Exception {
        String filename = "test-image.jpg";
        Resource mockResource = new ByteArrayResource("fake-bytes".getBytes());

        when(pictureService.loadImage(filename)).thenReturn(mockResource);

        mockMvc.perform(get("/api/images/{filename}", filename))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.IMAGE_JPEG_VALUE))
                .andExpect(content().bytes("fake-bytes".getBytes()));
    }

    @Test
    void getImage_ShouldReturnPngResource_WhenExtensionIsPng() throws Exception {
        String filename = "test-image.png";
        Resource mockResource = new ByteArrayResource("fake-bytes".getBytes());

        when(pictureService.loadImage(filename)).thenReturn(mockResource);

        mockMvc.perform(get("/api/images/{filename}", filename))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.IMAGE_PNG_VALUE));
    }
}