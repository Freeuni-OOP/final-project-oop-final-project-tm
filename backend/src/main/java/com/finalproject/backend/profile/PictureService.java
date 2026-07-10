package com.finalproject.backend.profile;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class PictureService {
    private final Path directory = Paths.get(System.getProperty("user.dir"), "Profile-pictures");
    private String fileExt(String s) {
        if(s != null && s.contains(".")) {
            return s.substring((s.lastIndexOf(".")));
        }
        return ".png";
    }

    public String saveImage(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID().toString() + fileExt(file.getOriginalFilename());

            Path destinationFile = this.directory.resolve(Paths.get(fileName)).normalize().toAbsolutePath();
            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Resource loadImage(String fileName) {
        try {
            System.out.println(directory.toAbsolutePath());
            Resource resource = new UrlResource(directory.resolve(fileName).toUri());
            if(!resource.isReadable()) {
                throw new RuntimeException("Not Readable");
            }
            return resource;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}