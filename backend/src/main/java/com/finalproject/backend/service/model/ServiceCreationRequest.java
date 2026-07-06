package com.finalproject.backend.service.model;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

public class ServiceCreationRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be under 100 characters")
    private String title;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Place is required")
    private String place;

    @NotBlank(message = "Price is required")
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$", message = "Price must be a valid number")
    private String price;

    @NotBlank(message = "Bio is required")
    @Size(max = 500, message = "Bio must be under 500 characters")
    private String bio;

    private MultipartFile profilePicture; // optional — no @NotBlank

    private String date; // optional

    @NotNull(message = "Max capacity is required")
    @Min(value = 1, message = "Max capacity must be at least 1")
    @Max(value = 100, message = "Max capacity must be at most 100")
    private Integer maxCapacity; // optional

    // Required no-arg constructor for @ModelAttribute binding
    public ServiceCreationRequest() {
    }

    // --- Getters and setters ---
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public MultipartFile getProfilePicture() { return profilePicture; }
    public void setProfilePicture(MultipartFile profilePicture) { this.profilePicture = profilePicture; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public Integer getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(Integer maxCapacity) { this.maxCapacity = maxCapacity; }
}