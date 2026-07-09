package com.finalproject.backend.modules.services.dtos;

public class listingResponseDto {
    private Integer id;
    private String title;
    private String category;
    private String description;
    private Double price;
    private String imageUrl;

    public listingResponseDto() {

    }

    //getters
    public Integer getId() { return id; }
    public String getTitle(){
        return title;
    }
    public String getCategory() { return category; }
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
    public void setId(Integer id) { this.id = id; }
    public void setTitle(String title){
        this.title = title;
    }
    public void setCategory(String category) { this.category = category; }
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
    public listingResponseDto(Integer id, String title, String category, String description, Double price, String imageUrl) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }
}