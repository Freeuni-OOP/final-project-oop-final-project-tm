package com.finalproject.backend.repositories;

import com.finalproject.backend.entities.Notification;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    public List<Notification> findAllByUserIdOrderByCreatedDesc(Integer userId);
    public List<Notification> findAllByUserId(Integer userId);
}
