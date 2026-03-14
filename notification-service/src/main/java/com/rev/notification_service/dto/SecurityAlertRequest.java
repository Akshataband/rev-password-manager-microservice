package com.rev.notification_service.dto;

import lombok.Data;

@Data
public class SecurityAlertRequest {

    private String username;
    private String alertType;
}