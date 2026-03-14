package com.rev.notification_service.dto;

import lombok.Data;

@Data
public class PasswordExpiryRequest {

    private String email;
    private String siteName;
}