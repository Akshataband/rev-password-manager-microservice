package com.rev.notification_service.controller;


import com.rev.notification_service.dto.NotificationRequest;
import com.rev.notification_service.dto.OtpNotificationRequest;
import com.rev.notification_service.dto.PasswordExpiryRequest;
import com.rev.notification_service.dto.SecurityAlertRequest;
import com.rev.notification_service.entity.Notification;
import com.rev.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;


    @PostMapping("/send-otp")
    public void sendOtp(@RequestBody OtpNotificationRequest request) {
        notificationService.sendOtp(request);
    }
    @GetMapping
    public List<Notification> getNotifications(
            @RequestParam String username) {

        return notificationService.getNotifications(username);
    }

    @PostMapping("/send")
    public String sendNotification(
            @RequestBody NotificationRequest request) {

        notificationService.sendNotification(request);
        return "Notification sent";
    }

    @PostMapping("/security-alert")
    public String securityAlert(
            @RequestBody SecurityAlertRequest request) {

        notificationService.sendSecurityAlert(request);
        return "Security alert sent";
    }

    @PutMapping("/read/{id}")
    public String markRead(@PathVariable Long id) {

        notificationService.markAsRead(id);
        return "Marked as read";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {

        notificationService.deleteNotification(id);
        return "Deleted";
    }
    @PostMapping("/password-expiry")
    public String passwordExpiry(
            @RequestBody PasswordExpiryRequest request) {

        notificationService.sendPasswordExpiryAlert(request);
        return "Password expiry alert sent";
    }
}