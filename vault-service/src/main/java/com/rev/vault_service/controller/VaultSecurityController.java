package com.rev.vault_service.controller;

import com.rev.vault_service.dto.ExpiringPasswordResponse;
import com.rev.vault_service.service.PasswordSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/passwords")
@RequiredArgsConstructor
public class VaultSecurityController {

    private final PasswordSecurityService securityService;

    @PostMapping("/{id}/reveal")
    public String revealPassword(@PathVariable Long id) {

        return securityService.revealPassword(id);
    }

    @GetMapping("/expiring")
    public List<ExpiringPasswordResponse> expiringPasswords() {

        return securityService.expiringPasswords();
    }
}