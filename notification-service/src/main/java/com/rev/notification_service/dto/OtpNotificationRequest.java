package com.rev.notification_service.dto;

import lombok.Data;

@Data
public class OtpNotificationRequest {

    private String email;
    private String otp;

}