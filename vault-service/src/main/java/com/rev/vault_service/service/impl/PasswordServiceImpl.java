package com.rev.vault_service.service;

import com.rev.vault_service.dto.PasswordRequest;
import com.rev.vault_service.dto.PasswordResponse;
import com.rev.vault_service.entity.Password;
import com.rev.vault_service.repository.PasswordRepository;
import com.rev.vault_service.security.CurrentUserUtil;
import com.rev.vault_service.util.EncryptionUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {

    private final PasswordRepository passwordRepository;
    private final EncryptionUtil encryptionUtil;

    @Override
    public PasswordResponse savePassword(PasswordRequest request) {

        String email = CurrentUserUtil.getCurrentUserEmail();

        String encryptedPassword = encryptionUtil.encrypt(request.getPassword());

        Password password = Password.builder()
                .userEmail(email)
                .siteName(request.getSiteName())
                .username(request.getUsername())
                .encryptedPassword(encryptedPassword)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Password saved = passwordRepository.save(password);

        return mapToResponse(saved);
    }

    @Override
    public List<PasswordResponse> getAllPasswords() {

        String email = CurrentUserUtil.getCurrentUserEmail();

        return passwordRepository.findByUserEmail(email)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PasswordResponse getPassword(Long id) {

        String email = CurrentUserUtil.getCurrentUserEmail();

        Password password = passwordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Password not found"));

        if (!password.getUserEmail().equals(email)) {
            throw new RuntimeException("Unauthorized access");
        }

        return mapToResponse(password);
    }

    @Override
    public PasswordResponse updatePassword(Long id, PasswordRequest request) {

        String email = CurrentUserUtil.getCurrentUserEmail();

        Password password = passwordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Password not found"));

        if (!password.getUserEmail().equals(email)) {
            throw new RuntimeException("Unauthorized access");
        }

        String encryptedPassword = encryptionUtil.encrypt(request.getPassword());

        password.setSiteName(request.getSiteName());
        password.setUsername(request.getUsername());
        password.setEncryptedPassword(encryptedPassword);
        password.setUpdatedAt(LocalDateTime.now());

        Password updated = passwordRepository.save(password);

        return mapToResponse(updated);
    }

    @Override
    public void deletePassword(Long id) {

        String email = CurrentUserUtil.getCurrentUserEmail();

        Password password = passwordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Password not found"));

        if (!password.getUserEmail().equals(email)) {
            throw new RuntimeException("Unauthorized access");
        }

        passwordRepository.delete(password);
    }

    private PasswordResponse mapToResponse(Password password) {

        String decryptedPassword = encryptionUtil.decrypt(password.getEncryptedPassword());

        return PasswordResponse.builder()
                .id(password.getId())
                .siteName(password.getSiteName())
                .username(password.getUsername())
                .password(decryptedPassword)
                .build();
    }
}