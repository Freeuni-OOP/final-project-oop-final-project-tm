package com.finalproject.backend.servicefeature.dao;

import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository // Tells Spring to use this for now
public class MockGetServiceImageDao {

    // Simulating a database table in memory
    private final Map<String, byte[]> imageDatabase = new HashMap<>();
    private final Map<String, String> contentTypeDatabase = new HashMap<>();

    public String saveImage(byte[] imageBytes, String contentType) {
        String id = UUID.randomUUID().toString(); // Generate a fake DB ID
        imageDatabase.put(id, imageBytes);
        contentTypeDatabase.put(id, contentType);
        return id;
    }

    public byte[] getImage(String imageId) {
        return imageDatabase.get(imageId);
    }

    public String getContentType(String imageId) {
        return contentTypeDatabase.get(imageId);
    }
}