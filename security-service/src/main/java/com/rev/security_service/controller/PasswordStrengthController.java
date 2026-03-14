package com.rev.security_service.controller;

import com.rev.security_service.dto.EncryptRequest;
import com.rev.security_service.dto.EncryptResponse;
import com.rev.security_service.dto.PasswordStrengthResponse;
import com.rev.security_service.service.EncryptionService;
import com.rev.security_service.service.PasswordStrengthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/security")
@RequiredArgsConstructor
public class PasswordStrengthController {

    private final PasswordStrengthService strengthService;
    private final EncryptionService encryptionService;

    @GetMapping("/strength")
    public PasswordStrengthResponse strength(@RequestParam String password) {

        return strengthService.analyze(password);
    }

    @PostMapping("/encrypt")
    public EncryptResponse encrypt(@RequestBody EncryptRequest request) {

        String encrypted = encryptionService.encrypt(request.getText());

        return new EncryptResponse(encrypted);
    }

    @PostMapping("/decrypt")
    public EncryptResponse decrypt(@RequestBody EncryptRequest request) {

        String decrypted = encryptionService.decrypt(request.getText());

        return new EncryptResponse(decrypted);
    }
}