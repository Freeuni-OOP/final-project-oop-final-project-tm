package com.finalproject.backend.modules.services;

import com.finalproject.backend.entities.Service;
import com.finalproject.backend.entities.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "favorites")
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @Column(name = "creation_time")
    private LocalDateTime creation_time = LocalDateTime.now();

    //getters
    public Integer getId() { return id; }
    public User getUser() { return user; }
    public Service getService() { return service; }
    public LocalDateTime getCreation_time() { return creation_time; }

    //setters
    public void setId(Integer id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setService(Service service) { this.service = service; }
    public void setCreation_time(LocalDateTime creation_time) { this.creation_time = creation_time; }
}