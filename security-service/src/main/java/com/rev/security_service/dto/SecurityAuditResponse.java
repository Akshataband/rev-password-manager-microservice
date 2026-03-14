package com.rev.security_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SecurityAuditResponse {

    private int totalPasswords;
    private int weakPasswords;
    private int reusedPasswords;
}