package com.rev.vault_service.service.impl;

import com.rev.vault_service.dto.PasswordRequest;
import com.rev.vault_service.dto.PasswordResponse;
import com.rev.vault_service.entity.Password;
import com.rev.vault_service.repository.PasswordRepository;
import com.rev.vault_service.security.CurrentUserUtil;
import com.rev.vault_service.service.PasswordService;
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
    private final CurrentUserUtil currentUserUtil;


    @Override
    public PasswordResponse savePassword(PasswordRequest request) {

        String email = currentUserUtil.getCurrentUserEmail();
        System.out.println("EMAIL FROM HEADER: " + email);

        if (email == null || email.isBlank()) {
            throw new RuntimeException("User email not found in request header");
        }

        String encryptedPassword = encryptionUtil.encrypt(request.getPassword());

        Password password = Password.builder()
                .userEmail(email)
                .siteName(request.getSiteName())
                .siteUrl(request.getSiteUrl())
                .username(request.getUsername())
                .encryptedPassword(encryptionUtil.encrypt(request.getPassword()))
                .category(request.getCategory())
                .notes(request.getNotes())
                .favorite(request.isFavorite())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Password saved = passwordRepository.save(password);

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
        password.setEncryptedPassword(encryptionUtil.encrypt(request.getPassword()));
        password.setCategory(request.getCategory());
        password.setNotes(request.getNotes());
        password.setFavorite(request.isFavorite());
        password.setUpdatedAt(LocalDateTime.now());

        Password updated = passwordRepository.save(password);

        return mapToResponse(updated);
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

    public List<PasswordResponse> search(String keyword){

        String email = currentUserUtil.getCurrentUserEmail();

        return passwordRepository
                .findByUserEmailAndSiteNameContainingIgnoreCase(email,keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<PasswordResponse> filterByCategory(String category){

        String email = currentUserUtil.getCurrentUserEmail();

        return passwordRepository
                .findByUserEmailAndCategory(email,category)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    public PasswordResponse getLast(){

        String email = currentUserUtil.getCurrentUserEmail();

        Password entry =
                passwordRepository
                        .findTopByUserEmailOrderByCreatedAtDesc(email)
                        .orElse(null);

        if(entry==null) return null;

        return mapToResponse(entry);
    }

}