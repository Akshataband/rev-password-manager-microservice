package com.rev.vault_service.controller;

import com.rev.vault_service.dto.PasswordRequest;
import com.rev.vault_service.dto.PasswordResponse;
import com.rev.vault_service.service.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/passwords")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordService passwordService;

    @PostMapping
    public PasswordResponse savePassword(
            @RequestBody PasswordRequest request) {

        return passwordService.savePassword(request);
    }

    @GetMapping
    public List<PasswordResponse> getAllPasswords() {

        return passwordService.getAllPasswords();
    }

    @GetMapping("/{id}")
    public PasswordResponse getPassword(@PathVariable Long id) {

        return passwordService.getPassword(id);
    }

    @PutMapping("/{id}")
    public PasswordResponse updatePassword(
            @PathVariable Long id,
            @RequestBody PasswordRequest request) {

        return passwordService.updatePassword(id, request);
    }

    @DeleteMapping("/{id}")
    public void deletePassword(@PathVariable Long id) {

        passwordService.deletePassword(id);
    }
}