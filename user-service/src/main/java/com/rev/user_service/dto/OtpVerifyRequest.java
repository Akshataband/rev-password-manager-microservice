package com.rev.user_service.dto;

import lombok.Data;

@Data
public class OtpVerifyRequest {

    private String email;
    private String otp;

}