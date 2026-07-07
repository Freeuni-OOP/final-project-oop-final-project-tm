package com.finalproject.backend.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="notifications")
public class Notification {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "txt")
    private String text;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "seen")
    private Boolean seen;

    public Notification() {
    }

    public Notification(Integer id, Integer userId, String text, LocalDateTime created, Boolean seen) {
        this.id = id;
        this.userId = userId;
        this.text = text;
        this.created = created;
        this.seen = seen;
    }

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }
}
