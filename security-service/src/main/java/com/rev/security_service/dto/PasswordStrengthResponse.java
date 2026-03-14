package com.rev.security_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordStrengthResponse {

    private String strength;
    private int score;
}