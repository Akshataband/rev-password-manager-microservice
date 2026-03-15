package com.rev.vault_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.rev.vault_service.dto.PasswordExpiryRequest;

@FeignClient(name = "notification-service")
public interface NotificationClient {

    // General notification
    @PostMapping("/api/notifications/send")
    void sendNotification(@RequestBody NotificationRequest request);

    // Password expiry notification
    @PostMapping("/api/notifications/password-expiry")
    void sendPasswordExpiry(@RequestBody PasswordExpiryRequest request);
}