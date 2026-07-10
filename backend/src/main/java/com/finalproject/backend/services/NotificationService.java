package com.finalproject.backend.services;

import com.finalproject.backend.entities.Notification;
import com.finalproject.backend.profile.DTO.NotificationDTO;
import com.finalproject.backend.repositories.NotificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    // adds notification as a string to userId, the date is added inside
    public void addNotification(Integer userId, String text) {
        Notification notification = new Notification();
        notification.setCreated(LocalDateTime.now());
        notification.setUserId(userId);
        notification.setSeen(false);
        notification.setText(text);

        notificationRepository.save(notification);
    }

    // Gets sorted notification by userId
    public List<NotificationDTO> getNotification(Integer userId) {
        List<Notification> l = notificationRepository.findAllByUserIdOrderByCreatedDesc(userId);
        List<NotificationDTO> res = new ArrayList<>();
        for(Notification n : l) {
            res.add(new NotificationDTO(n.getId(), n.getUserId(), n.getText(), n.getCreated(), n.getSeen()));
        }
        return res;
    }


    // Because of Transaction, changes are automatically saved
    @Transactional
    public void setSeen(Integer id) {
        List<Notification> notification = notificationRepository.findAllByUserId(id);
        notification.forEach(notification1 -> notification1.setSeen(true));
        notificationRepository.saveAll(notification);
    }
}
