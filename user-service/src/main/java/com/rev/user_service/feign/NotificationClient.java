package com.rev.user_service.feign;

import com.rev.notification_service.dto.OtpNotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service")
public interface NotificationClient {

    @PostMapping("/api/notifications/send-otp")
    void sendOtp(@RequestBody OtpNotificationRequest request);

}