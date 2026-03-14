package com.rev.user_service.dto;

import lombok.Data;

@Data
public class ResetPasswordRequest {

    private String email;
    private String newPassword;

}