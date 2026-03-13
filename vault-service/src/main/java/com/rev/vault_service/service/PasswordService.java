package com.rev.vault_service.service;

import com.rev.vault_service.dto.PasswordRequest;
import com.rev.vault_service.dto.PasswordResponse;

import java.util.List;

public interface PasswordService {

    PasswordResponse savePassword(PasswordRequest request);

    List<PasswordResponse> getAllPasswords();

    PasswordResponse getPassword(Long id);

    PasswordResponse updatePassword(Long id, PasswordRequest request);

    void deletePassword(Long id);
}