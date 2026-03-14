package com.rev.vault_service.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrentUserUtil {

    private final HttpServletRequest request;

    public String getCurrentUserEmail() {

        String email = request.getHeader("X-User-Email");

        if (email == null || email.isBlank()) {
            throw new RuntimeException("X-User-Email header missing");
        }

        return email;
    }
}