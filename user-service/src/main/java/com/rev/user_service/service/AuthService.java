package com.rev.user_service.service;

import com.rev.notification_service.dto.OtpNotificationRequest;
import com.rev.user_service.dto.LoginRequest;
import com.rev.user_service.dto.OtpVerifyRequest;
import com.rev.user_service.entity.OtpVerification;
import com.rev.user_service.entity.User;
import com.rev.user_service.repository.UserRepository;
import com.rev.user_service.repository.OtpRepository;
import com.rev.user_service.security.JwtUtil;
import com.rev.user_service.feign.NotificationClient;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final NotificationClient notificationClient;
    private final OtpRepository otpRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public void login(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        otpRepository.deleteByEmail(request.getEmail());

        String otp = otpService.generateOtp();

        otpService.saveOtp(request.getEmail(), otp);

        OtpNotificationRequest notification = new OtpNotificationRequest();
        notification.setEmail(request.getEmail());
        notification.setOtp(otp);

        notificationClient.sendOtp(notification);
    }

    public String verifyOtp(OtpVerifyRequest request) {

        OtpVerification otp = otpRepository
                .findTopByEmailOrderByExpiryTimeDesc(request.getEmail())
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (!otp.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        if (otp.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        return jwtUtil.generateToken(request.getEmail());
    }
}