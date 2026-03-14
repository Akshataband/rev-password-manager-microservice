package com.rev.security_service.controller;

import com.rev.security_service.dto.*;
import com.rev.security_service.service.SecurityAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/security")
@RequiredArgsConstructor
public class SecurityAuditController {

    private final SecurityAuditService auditService;

    @GetMapping("/audit")
    public SecurityAuditResponse audit() {
        return auditService.audit();
    }

    @GetMapping("/weak-passwords")
    public List<WeakPasswordDto> weakPasswords() {
        return auditService.weakPasswords();
    }

    @GetMapping("/reused-passwords")
    public List<ReusedPasswordDto> reusedPasswords() {
        return auditService.reusedPasswords();
    }
}