package com.finalproject.backend.modules.services;
import com.finalproject.backend.modules.users.user;
import jakarta.persistence.*;

@Entity
@Table(name = "services")
public class listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer serviceId;

    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private user provider;

    private String title, bio, pictureUrl;
    private Double price;
    private String category;

    public listing(){

    }

    //getters
    public Integer getServiceId(){
        return serviceId;
    }
    public user getProvider(){
        return provider;
    }
    public String getTitle(){
        return title;
    }
    public String getBio(){
        return bio;
    }
    public String getPictureUrl(){
        return pictureUrl;
    }
    public Double getPrice(){
        return price;
    }
    public String getCategory(){
        return category;
    }

    //setters
    public void setServiceId(Integer serviceId){
        this.serviceId = serviceId;
    }
    public void setProvider(user provider){
        this.provider = provider;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setBio(String bio){
        this.bio = bio;
    }
    public void setPictureUrl(String pictureUrl){
        this.pictureUrl = pictureUrl;
    }
    public void setPrice(Double price){
        this.price = price;
    }
    public void setCategory(String category){
        this.category = category;
    }
}