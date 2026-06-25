package com.finalproject.backend.modules.services.dtos;

public class listingResponseDto {
    private String title;
    private String description;
    private Double price;
    private String imageUrl;

    public listingResponseDto() {

    }

    //getters
    public String getTitle(){
        return title;
    }
    public String getDescription(){
        return description;
    }
    public Double getPrice(){
        return price;
    }
    public String getImageUrl(){
        return imageUrl;
    }

    //setters
    public void setTitle(String title){
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setPrice(Double price){
        this.price = price;
    }
    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    //create listing response with all fields
    public listingResponseDto(String title, String description, Double price, String imageUrl) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }
}