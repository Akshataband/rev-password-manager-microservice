package com.rev.password_service.service;

import com.rev.password_service.dto.PasswordRequest;
import com.rev.password_service.dto.PasswordResponse;

import java.util.List;

public interface PasswordService {

    PasswordResponse savePassword(Long userId, PasswordRequest request);

    List<PasswordResponse> getAllPasswords(Long userId);

    PasswordResponse getPassword(Long userId, Long id);

    PasswordResponse updatePassword(Long userId, Long id, PasswordRequest request);

    void deletePassword(Long userId, Long id);

}