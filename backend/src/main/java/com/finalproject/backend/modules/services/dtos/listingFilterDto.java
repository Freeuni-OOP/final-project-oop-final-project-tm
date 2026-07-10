package com.finalproject.backend.modules.services.dtos;

public class listingFilterDto {
    private String text;
    private Double min;
    private Double max;
    private Long providerId;
    private String category;
    private Long favoriteUserId;
    private Long excludeFavUserId;

    //getters
    public String getText(){
        return text;
    }
    public String getCategory(){
        return category;
    }
    public Double getMin(){
        return min;
    }
    public Double getMax(){
        return max;
    }
    public Long getFavoriteUserId(){
        return favoriteUserId;
    }
    public Long getExcludeFavUserId(){
        return excludeFavUserId;
    }
    public Long getProviderId() {
        return providerId;
    }

    //setters
    public void setText(String text){
        this.text = text;
    }
    public void setCategory(String category){
        this.category = category;
    }
    public void setMin(Double min){
        this.min = min;
    }
    public void setMax(Double max){
        this.max = max;
    }
    public void setFavoriteUserId(Long favoriteUserId){
        this.favoriteUserId = favoriteUserId;
    }
    public void setExcludeFavUserId(Long excludeUserId){
        this.excludeFavUserId = excludeUserId;
    }
    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }
}