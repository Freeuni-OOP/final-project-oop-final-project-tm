package com.finalproject.backend.profile.DTO;

public class ServiceDTO {
    private Integer serviceId;
    private String title;
    private String category;
    private Double price;
    private String status;

    public ServiceDTO(Integer serviceId, String title, String category, Double price, String status) {
        this.serviceId = serviceId;
        this.title = title;
        this.category = category;
        this.price = price;
        this.status = status;
    }

    public Integer getServiceId(){
        return serviceId;
    }
    public String getTitle(){
        return title;
    }
    public String getCategory(){
        return category;
    }
    public Double getPrice(){
        return price;
    }
    public String getStatus(){
        return status;
    }
}