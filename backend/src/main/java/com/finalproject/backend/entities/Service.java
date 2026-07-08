package com.finalproject.backend.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "services")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private User providerId;

    @Column(name = "title")
    private String title;

    @Column(name = "bio")
    private String bio;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "created")
    private LocalDateTime timePosted;

    @Column(name = "price")
    private Double price;

    @Column(name = "category")
    private String category;

    @Column(name = "address")
    private String address;

    @Column(name = "max_capacity")
    private Integer maxCapacity;

    @Column(name = "active", columnDefinition = "boolean default true")
    private Boolean active;

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setProviderId(User providerId) {
        this.providerId = providerId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setTimePosted(LocalDateTime timePosted) {
        this.timePosted = timePosted;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setActive(Boolean active) { this.active = active; }

    public Integer getId() {
        return id;
    }

    public User getProviderId() {
        return providerId;
    }

    public String getTitle() {
        return title;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getBio() {
        return bio;
    }

    public LocalDateTime getTimePosted() {
        return timePosted;
    }

    public Double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getAddress() {
        return address;
    }

    public Boolean getActive() { return active; }
}
