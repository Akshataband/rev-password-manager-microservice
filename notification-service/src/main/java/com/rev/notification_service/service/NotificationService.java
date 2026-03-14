package com.rev.notification_service.service;

import com.rev.notification_service.dto.NotificationRequest;
import com.rev.notification_service.dto.SecurityAlertRequest;
import com.rev.notification_service.entity.Notification;
import com.rev.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repo;

    public void sendNotification(NotificationRequest request) {

        Notification notification = new Notification();

        notification.setUsername(request.getUsername());
        notification.setMessage(request.getMessage());
        notification.setType(request.getType());
        notification.setCreatedAt(LocalDateTime.now());
        notification.setReadStatus(false);

        repo.save(notification);
    }

    public void sendSecurityAlert(SecurityAlertRequest request) {

        Notification notification = new Notification();

        notification.setUsername(request.getUsername());
        notification.setMessage(request.getAlertType());
        notification.setType("SECURITY_ALERT");
        notification.setCreatedAt(LocalDateTime.now());
        notification.setReadStatus(false);

        repo.save(notification);
    }

    public List<Notification> getNotifications(String username) {
        return repo.findByUsername(username);
    }

    public void markAsRead(Long id) {

        Notification notification = repo.findById(id)
                .orElseThrow();

        notification.setReadStatus(true);

        repo.save(notification);
    }

    public void deleteNotification(Long id) {

        repo.deleteById(id);
    }
}