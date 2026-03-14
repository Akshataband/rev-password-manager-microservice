package com.rev.user_service.controller;

import com.rev.user_service.dto.JwtResponse;
import com.rev.user_service.dto.LoginRequest;
import com.rev.user_service.dto.OtpVerifyRequest;
import com.rev.user_service.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        authService.login(request);
        return ResponseEntity.ok("OTP sent to email");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerifyRequest request) {

        String token = authService.verifyOtp(request);

        return ResponseEntity.ok(new JwtResponse(token));
    }
}