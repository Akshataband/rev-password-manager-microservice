package com.rev.password_service.service.impl;

import com.rev.password_service.dto.PasswordRequest;
import com.rev.password_service.dto.PasswordResponse;
import com.rev.password_service.entity.Password;
import com.rev.password_service.repository.PasswordRepository;
import com.rev.password_service.service.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {

    private final PasswordRepository passwordRepository;

    @Override
    public PasswordResponse savePassword(Long userId, PasswordRequest request) {

        Password password = Password.builder()
                .userId(userId)
                .siteName(request.getSiteName())
                .username(request.getUsername())
                .encryptedPassword(request.getPassword())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Password saved = passwordRepository.save(password);

        return mapToResponse(saved);
    }

    @Override
    public List<PasswordResponse> getAllPasswords(Long userId) {

        return passwordRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PasswordResponse getPassword(Long userId, Long id) {

        Password password = passwordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Password not found"));

        return mapToResponse(password);
    }

    @Override
    public PasswordResponse updatePassword(Long userId, Long id, PasswordRequest request) {

        Password password = passwordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Password not found"));

        password.setSiteName(request.getSiteName());
        password.setUsername(request.getUsername());
        password.setEncryptedPassword(request.getPassword());
        password.setUpdatedAt(LocalDateTime.now());

        Password updated = passwordRepository.save(password);

        return mapToResponse(updated);
    }

    @Override
    public void deletePassword(Long userId, Long id) {

        passwordRepository.deleteById(id);
    }

    private PasswordResponse mapToResponse(Password password) {

        return PasswordResponse.builder()
                .id(password.getId())
                .siteName(password.getSiteName())
                .username(password.getUsername())
                .password(password.getEncryptedPassword())
                .build();
    }
}