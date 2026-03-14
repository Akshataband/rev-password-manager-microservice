package com.rev.notification_service.dto;

import lombok.Data;

@Data
public class NotificationRequest {

    private String username;
    private String message;
    private String type;

}