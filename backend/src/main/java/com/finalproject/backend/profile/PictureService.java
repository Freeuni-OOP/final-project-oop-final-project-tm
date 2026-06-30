package com.finalproject.backend.profile;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PictureService {

    private final Path location = Paths.get("Profile-pictures").toAbsolutePath().normalize();

    public Resource loadImage(String name) throws MalformedURLException {
        Path file = location.resolve(name).normalize();

        if (!file.startsWith(location)) {
            System.out.println("Security block: Attempted to access file outside directory.");
            return null;
        }

        System.out.println("Looking for file at: " + file.toString());
        Resource resource = new UrlResource(file.toUri());

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            System.out.println("File does not exist or is not readable.");
            return null;
        }
    }
}