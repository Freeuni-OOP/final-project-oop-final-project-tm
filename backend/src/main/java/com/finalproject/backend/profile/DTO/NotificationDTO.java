package com.finalproject.backend.profile.DTO;

import java.time.LocalDateTime;

public class NotificationDTO {
    private Integer id;
    private Integer userId;
    private String text;
    private LocalDateTime created;
    private Boolean seen;

    public NotificationDTO() {
    }

    public NotificationDTO(Integer id, Integer userId, String text, LocalDateTime created, Boolean seen) {
        this.id = id;
        this.userId = userId;
        this.text = text;
        this.created = created;
        this.seen = seen;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
