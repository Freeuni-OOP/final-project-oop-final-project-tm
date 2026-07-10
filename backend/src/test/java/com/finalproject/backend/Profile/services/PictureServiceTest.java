package com.finalproject.backend.Profile.services;

import com.finalproject.backend.profile.PictureService;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PictureServiceTest {

    @TempDir
    Path tempDir;

    private String originalUserDir;
    private PictureService pictureService;

    @BeforeEach
    void setUp() throws IOException {
        originalUserDir = System.getProperty("user.dir");
        System.setProperty("user.dir", tempDir.toAbsolutePath().toString());
        Files.createDirectories(tempDir.resolve("Profile-pictures"));
        pictureService = new PictureService();
    }

    @AfterEach
    void tearDown() {
        if (originalUserDir != null) {
            System.setProperty("user.dir", originalUserDir);
        }
    }

    @Test
    void saveSuccess() {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "data".getBytes());

        String result = pictureService.saveImage(file);

        assertNotNull(result);
        assertTrue(result.endsWith(".jpg"));
        assertTrue(Files.exists(tempDir.resolve("Profile-pictures").resolve(result)));
    }

    @Test
    void saveNoExtension() {
        MockMultipartFile file = new MockMultipartFile("file", "test", "image/png", "data".getBytes());

        String result = pictureService.saveImage(file);

        assertNotNull(result);
        assertTrue(result.endsWith(".png"));
        assertTrue(Files.exists(tempDir.resolve("Profile-pictures").resolve(result)));
    }

    @Test
    void saveNullName() {
        MockMultipartFile file = new MockMultipartFile("file", null, "image/png", "data".getBytes());

        String result = pictureService.saveImage(file);

        assertNotNull(result);
        assertTrue(result.endsWith(".png"));
    }

    @Test
    void saveFailure() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.jpg");
        when(file.getInputStream()).thenThrow(new IOException());

        assertThrows(RuntimeException.class, () -> pictureService.saveImage(file));
    }

    @Test
    void loadSuccess() throws IOException {
        String filename = "avatar.png";
        Path file = tempDir.resolve("Profile-pictures").resolve(filename);
        Files.writeString(file, "content");

        Resource resource = pictureService.loadImage(filename);

        assertNotNull(resource);
        assertTrue(resource.exists());
        assertTrue(resource.isReadable());
    }

    @Test
    void loadFailure() {
        assertThrows(RuntimeException.class, () -> pictureService.loadImage("missing.png"));
    }
}