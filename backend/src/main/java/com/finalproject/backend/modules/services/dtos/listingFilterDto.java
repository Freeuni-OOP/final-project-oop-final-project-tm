package com.finalproject.backend.modules.services.dtos;

public class listingFilterDto {
    private String text;
    private Double min;
    private Double max;
    private String category;

    //getters
    public String getText(){
        return text;
    }
    public Double getMin(){
        return min;
    }
    public Double getMax(){
        return max;
    }
    public String getCategory(){
        return category;
    }

    //setters
    public void setText(String text){
        this.text = text;
    }
    public void setMin(Double min){
        this.min = min;
    }
    public void setMax(Double max){
        this.max = max;
    }
    public void setCategory(String category){
        this.category = category;
    }
}