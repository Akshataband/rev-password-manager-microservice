package com.rev.vault_service.service.impl;

import com.rev.vault_service.client.NotificationClient;
import com.rev.vault_service.client.NotificationRequest;
import com.rev.vault_service.dto.PasswordRequest;
import com.rev.vault_service.dto.PasswordResponse;
import com.rev.vault_service.entity.Password;
import com.rev.vault_service.repository.PasswordRepository;
import com.rev.vault_service.security.CurrentUserUtil;
import com.rev.vault_service.service.PasswordService;
import com.rev.vault_service.util.EncryptionUtil;
import com.rev.vault_service.util.PasswordExpiryUtil;
import com.rev.vault_service.util.PasswordStrengthUtil;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {

    private final PasswordRepository passwordRepository;
    private final EncryptionUtil encryptionUtil;
    private final CurrentUserUtil currentUserUtil;
    private final NotificationClient notificationClient;
    private final PasswordStrengthUtil passwordStrengthUtil;
    private final PasswordExpiryUtil passwordExpiryUtil;

    private static final Logger log = LoggerFactory.getLogger(PasswordServiceImpl.class);

    @Override
    public PasswordResponse savePassword(PasswordRequest request) {

        String email = currentUserUtil.getCurrentUserEmail();

        if (email == null || email.isBlank()) {
            throw new RuntimeException("User email not found in request header");
        }

        String encryptedPassword = encryptionUtil.encrypt(request.getPassword());

        // Check password reuse
        boolean reused = passwordRepository
                .existsByUserEmailAndEncryptedPassword(email, encryptedPassword);

        if (reused) {

            log.warn("Password reuse detected for site {} by user {}", request.getSiteName(), email);

            NotificationRequest notification = new NotificationRequest();
            notification.setUsername(email);
            notification.setMessage("Password reused across multiple sites: " + request.getSiteName());
            notification.setType("WARNING");

            sendNotification(notification);
        }

        // Weak password detection
        if (passwordStrengthUtil.isWeak(request.getPassword())) {

            log.warn("Weak password detected for site {} by user {}", request.getSiteName(), email);

            NotificationRequest notification = new NotificationRequest();
            notification.setUsername(email);
            notification.setMessage("Weak password detected for site: " + request.getSiteName());
            notification.setType("WARNING");

            sendNotification(notification);
        }

        Password password = Password.builder()
                .userEmail(email)
                .siteName(request.getSiteName())
                .siteUrl(request.getSiteUrl())
                .username(request.getUsername())
                .encryptedPassword(encryptedPassword)
                .category(request.getCategory())
                .notes(request.getNotes())
                .favorite(request.isFavorite())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Password saved = passwordRepository.save(password);

        log.info("Password saved for site {} by user {}", request.getSiteName(), email);

        NotificationRequest notification = new NotificationRequest();
        notification.setUsername(email);
        notification.setMessage("New password saved for site: " + request.getSiteName());
        notification.setType("INFO");

        sendNotification(notification);

        return mapToResponse(saved);
    }

    @Override
    public List<PasswordResponse> getAllPasswords() {

        String email = currentUserUtil.getCurrentUserEmail();

        return passwordRepository.findByUserEmail(email)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PasswordResponse getPassword(Long id) {

        String email = currentUserUtil.getCurrentUserEmail();

        Password password = passwordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Password not found"));

        if (!password.getUserEmail().equals(email)) {
            throw new RuntimeException("Unauthorized access");
        }

        // Password expiry check
        if (passwordExpiryUtil.isPasswordExpired(password.getCreatedAt())) {

            log.warn("Password expired for site {} user {}", password.getSiteName(), email);

            NotificationRequest notification = new NotificationRequest();
            notification.setUsername(email);
            notification.setMessage(
                    "Password for site " + password.getSiteName()
                            + " is older than 90 days. Consider updating it."
            );
            notification.setType("WARNING");

            sendNotification(notification);
        }

        return mapToResponse(password);
    }

    @Override
    public PasswordResponse updatePassword(Long id, PasswordRequest request) {

        String email = currentUserUtil.getCurrentUserEmail();

        Password password = passwordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Password not found"));

        if (!password.getUserEmail().equals(email)) {
            throw new RuntimeException("Unauthorized access");
        }

        String encryptedPassword = encryptionUtil.encrypt(request.getPassword());

        password.setSiteName(request.getSiteName());
        password.setSiteUrl(request.getSiteUrl());
        password.setUsername(request.getUsername());
        password.setEncryptedPassword(encryptedPassword);
        password.setCategory(request.getCategory());
        password.setNotes(request.getNotes());
        password.setFavorite(request.isFavorite());
        password.setUpdatedAt(LocalDateTime.now());

        passwordRepository.save(password);

        log.info("Password updated for site {} by user {}", request.getSiteName(), email);

        NotificationRequest notification = new NotificationRequest();
        notification.setUsername(email);
        notification.setMessage("Password updated for site: " + request.getSiteName());
        notification.setType("INFO");

        sendNotification(notification);

        return mapToResponse(password);
    }

    @Override
    public void deletePassword(Long id) {

        String email = currentUserUtil.getCurrentUserEmail();

        Password password = passwordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Password not found"));

        if (!password.getUserEmail().equals(email)) {
            throw new RuntimeException("Unauthorized access");
        }

        passwordRepository.delete(password);

        log.warn("Password deleted for site {} by user {}", password.getSiteName(), email);

        NotificationRequest notification = new NotificationRequest();
        notification.setUsername(email);
        notification.setMessage("Password deleted for site: " + password.getSiteName());
        notification.setType("WARNING");

        sendNotification(notification);
    }

    private PasswordResponse mapToResponse(Password password) {

        String decryptedPassword =
                encryptionUtil.decrypt(password.getEncryptedPassword());

        return PasswordResponse.builder()
                .id(password.getId())
                .siteName(password.getSiteName())
                .siteUrl(password.getSiteUrl())
                .username(password.getUsername())
                .password(decryptedPassword)
                .category(password.getCategory())
                .notes(password.getNotes())
                .favorite(password.isFavorite())
                .createdAt(password.getCreatedAt())
                .updatedAt(password.getUpdatedAt())
                .build();
    }

    public List<PasswordResponse> getFavorites() {

        String email = currentUserUtil.getCurrentUserEmail();

        return passwordRepository.findByUserEmailAndFavoriteTrue(email)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<PasswordResponse> search(String keyword) {

        String email = currentUserUtil.getCurrentUserEmail();

        return passwordRepository
                .findByUserEmailAndSiteNameContainingIgnoreCase(email, keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<PasswordResponse> filterByCategory(String category) {

        String email = currentUserUtil.getCurrentUserEmail();

        return passwordRepository
                .findByUserEmailAndCategory(email, category)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PasswordResponse getLast() {

        String email = currentUserUtil.getCurrentUserEmail();

        Password entry =
                passwordRepository
                        .findTopByUserEmailOrderByCreatedAtDesc(email)
                        .orElse(null);

        if (entry == null) return null;

        return mapToResponse(entry);
    }

    // Circuit Breaker
    @CircuitBreaker(name = "notificationService", fallbackMethod = "notificationFallback")
    public void sendNotification(NotificationRequest request) {

        notificationClient.sendNotification(request);
    }

    public void notificationFallback(NotificationRequest request, Throwable ex) {

        log.error("Notification service unavailable. Skipping notification.");
    }
}