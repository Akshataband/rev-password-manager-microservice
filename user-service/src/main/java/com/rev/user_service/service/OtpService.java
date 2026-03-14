package com.rev.user_service.service;

import com.rev.user_service.entity.OtpVerification;
import com.rev.user_service.repository.OtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpRepository otpRepository;

    public String generateOtp() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    public void saveOtp(String email, String otp) {

        OtpVerification verification = new OtpVerification();

        verification.setEmail(email);
        verification.setOtp(otp);
        verification.setExpiryTime(LocalDateTime.now().plusMinutes(5));

        otpRepository.save(verification);
    }
}