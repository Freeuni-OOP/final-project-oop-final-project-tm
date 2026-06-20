package com.finalproject.backend.entities;

import jakarta.persistence.*;

@Entity
public class Services {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "serviece_id")
    private int id;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "title")
    private String title;

    @Column(name = "bio")
    private String bio;

    @Column(name = "picture_url")
    private String picUrl;

    @Column(name = "created")
    private String timePosted;
}
