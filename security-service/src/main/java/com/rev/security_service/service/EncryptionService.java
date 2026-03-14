package com.rev.security_service.service;

import org.springframework.stereotype.Service;
import java.util.Base64;

@Service
public class EncryptionService {

    public String encrypt(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes());
    }

    public String decrypt(String text) {
        return new String(Base64.getDecoder().decode(text));
    }
}