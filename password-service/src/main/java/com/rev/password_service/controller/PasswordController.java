package com.rev.password_service.controller;

import com.rev.password_service.dto.PasswordRequest;
import com.rev.password_service.dto.PasswordResponse;
import com.rev.password_service.service.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/passwords")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordService passwordService;

    @PostMapping("/{userId}")
    public PasswordResponse savePassword(
            @PathVariable Long userId,
            @RequestBody PasswordRequest request) {

        return passwordService.savePassword(userId, request);
    }

    @GetMapping("/{userId}")
    public List<PasswordResponse> getAllPasswords(@PathVariable Long userId) {

        return passwordService.getAllPasswords(userId);
    }

    @GetMapping("/{userId}/{id}")
    public PasswordResponse getPassword(
            @PathVariable Long userId,
            @PathVariable Long id) {

        return passwordService.getPassword(userId, id);
    }

    @PutMapping("/{userId}/{id}")
    public PasswordResponse updatePassword(
            @PathVariable Long userId,
            @PathVariable Long id,
            @RequestBody PasswordRequest request) {

        return passwordService.updatePassword(userId, id, request);
    }

    @DeleteMapping("/{userId}/{id}")
    public void deletePassword(
            @PathVariable Long userId,
            @PathVariable Long id) {

        passwordService.deletePassword(userId, id);
    }
}