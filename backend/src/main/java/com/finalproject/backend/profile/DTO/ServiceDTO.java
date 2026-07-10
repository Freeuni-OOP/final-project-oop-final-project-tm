package com.finalproject.backend.profile.DTO;

import com.finalproject.backend.entities.Service;

import java.time.LocalDateTime;

public class ServiceDTO {

    private Integer id;
    private Integer providerId;
    private String providerName;
    private String title;
    private String bio;
    private String imagePath;
    private LocalDateTime timePosted;
    private Double price;
    private String category;
    private String address;
    private Integer maxCapacity;
    private Boolean active;
    private Integer star;

    public ServiceDTO() {
    }

    //getters
    public ServiceDTO(Service service) {
        this.id = service.getId();
        if (service.getProviderId() != null) {
            this.providerId = service.getProviderId().getId();
        }
        this.title = service.getTitle();
        this.bio = service.getBio();
        this.imagePath = service.getImagePath();
        this.timePosted = service.getTimePosted();
        this.price = service.getPrice();
        this.category = service.getCategory();
        this.address = service.getAddress();
        this.maxCapacity = service.getMaxCapacity();
        this.active = service.getActive();
        this.star = service.getStar();
    }

    //setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public Integer getId() {
        return id;
    }

    public Integer getProviderId() {
        return providerId;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getTitle() {
        return title;
    }

    public String getBio() {
        return bio;
    }

    public String getImagePath() {
        return imagePath;
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

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public Boolean getActive() {
        return active;
    }

    public Integer getStar() {
        return star;
    }
}