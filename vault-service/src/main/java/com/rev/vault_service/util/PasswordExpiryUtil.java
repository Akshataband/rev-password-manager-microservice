package com.rev.vault_service.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class PasswordExpiryUtil {

    private static final int EXPIRY_DAYS = 90;

    public boolean isPasswordExpired(LocalDateTime createdAt) {

        long days = ChronoUnit.DAYS.between(createdAt, LocalDateTime.now());

        return days >= EXPIRY_DAYS;
    }
}