package com.finalproject.backend.modules.services;

import com.finalproject.backend.modules.users.user;
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
    private user user;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private listing service;

    @Column(name = "creation_time")
    private LocalDateTime creation_time = LocalDateTime.now();

    //getters
    public Integer getId() { return id; }
    public user getUser() { return user; }
    public listing getService() { return service; }
    public LocalDateTime getCreation_time() { return creation_time; }

    //setters
    public void setId(Integer id) { this.id = id; }
    public void setUser(user user) { this.user = user; }
    public void setService(listing service) { this.service = service; }
    public void setCreation_time(LocalDateTime creation_time) { this.creation_time = creation_time; }
}