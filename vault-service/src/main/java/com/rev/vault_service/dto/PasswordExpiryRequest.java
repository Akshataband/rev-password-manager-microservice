package com.rev.vault_service.dto;

import lombok.Data;

@Data
public class PasswordExpiryRequest {

    private String email;
    private String siteName;
}