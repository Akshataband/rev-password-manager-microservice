package com.rev.vault_service.service;

import com.rev.vault_service.dto.ExpiringPasswordResponse;
import com.rev.vault_service.entity.Password;
import com.rev.vault_service.repository.PasswordRepository;
import com.rev.vault_service.security.CurrentUserUtil;
import com.rev.vault_service.util.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PasswordSecurityService {

    private final PasswordRepository passwordRepository;
    private final CurrentUserUtil currentUserUtil;
    private final EncryptionUtil encryptionUtil;

    public String revealPassword(Long id) {

        String email = currentUserUtil.getCurrentUserEmail();

        Password password = passwordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Password not found"));

        if (!password.getUserEmail().equals(email)) {
            throw new RuntimeException("Unauthorized access");
        }

        return encryptionUtil.decrypt(password.getEncryptedPassword());
    }

    public List<ExpiringPasswordResponse> expiringPasswords() {

        String email = currentUserUtil.getCurrentUserEmail();

        List<Password> passwords =
                passwordRepository.findByUserEmail(email);

        return passwords.stream()
                .filter(p -> {
                    long days =
                            Duration.between(
                                    p.getCreatedAt(),
                                    LocalDateTime.now()
                            ).toDays();

                    return days > 90;
                })
                .map(p -> new ExpiringPasswordResponse(
                        p.getId(),
                        p.getSiteName(),
                        (int) Duration.between(
                                p.getCreatedAt(),
                                LocalDateTime.now()
                        ).toDays()
                ))
                .collect(Collectors.toList());
    }
}