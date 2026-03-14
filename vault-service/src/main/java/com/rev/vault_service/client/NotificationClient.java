package com.rev.vault_service.client;

import org.springframework.cloud.openfeign.FeignClient;

import com.rev.vault_service.dto.PasswordExpiryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service")
public interface NotificationClient {

    @PostMapping("/api/notifications/password-expiry")
    void sendPasswordExpiry(@RequestBody PasswordExpiryRequest request);

}