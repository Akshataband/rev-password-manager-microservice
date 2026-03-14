package com.rev.vault_service.service;

import com.rev.vault_service.client.NotificationClient;
import com.rev.vault_service.dto.PasswordExpiryRequest;
import com.rev.vault_service.entity.Password;
import com.rev.vault_service.repository.PasswordRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PasswordExpiryScheduler {

    private final PasswordRepository passwordRepository;
    private final NotificationClient notificationClient;

    @Scheduled(cron = "0 0 2 * * ?") // every day at 2AM
    public void checkPasswordExpiry() {

        List<Password> passwords = passwordRepository.findAll();

        for (Password password : passwords) {

            long days = Duration.between(
                    password.getCreatedAt(),
                    LocalDateTime.now()
            ).toDays();

            if (days > 90) {

                PasswordExpiryRequest request = new PasswordExpiryRequest();

                request.setEmail(password.getUserEmail());
                request.setSiteName(password.getSiteName());

                notificationClient.sendPasswordExpiry(request);
            }
        }
    }
}