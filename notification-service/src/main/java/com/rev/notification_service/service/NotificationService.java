package com.rev.notification_service.service;

import com.rev.notification_service.dto.NotificationRequest;
import com.rev.notification_service.dto.OtpNotificationRequest;
import com.rev.notification_service.dto.SecurityAlertRequest;
import com.rev.notification_service.dto.PasswordExpiryRequest;   // ⭐ ADD THIS
import com.rev.notification_service.entity.Notification;
import com.rev.notification_service.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repo;
    private final JavaMailSender mailSender;

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

    public void sendOtp(OtpNotificationRequest request) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(request.getEmail());
        message.setSubject("Password Manager OTP Verification");
        message.setText("Your OTP is: " + request.getOtp() + "\nValid for 5 minutes.");

        mailSender.send(message);
    }

    // ⭐ Password expiry alert
    public void sendPasswordExpiryAlert(PasswordExpiryRequest request) {

        Notification notification = new Notification();

        notification.setUsername(request.getEmail());
        notification.setMessage(
                "Your password for " + request.getSiteName() +
                        " is older than 90 days. Please update it."
        );

        notification.setType("PASSWORD_EXPIRY");
        notification.setCreatedAt(LocalDateTime.now());
        notification.setReadStatus(false);

        repo.save(notification);

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(request.getEmail());
        message.setSubject("Password Expiry Warning");

        message.setText(
                "Your password for site " +
                        request.getSiteName() +
                        " is older than 90 days.\n\nPlease update it for better security."
        );

        mailSender.send(message);
    }
}