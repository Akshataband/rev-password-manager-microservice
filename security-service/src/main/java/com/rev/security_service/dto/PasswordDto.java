package com.rev.security_service.dto;

import lombok.Data;

@Data
public class PasswordDto {

    private Long id;
    private String siteName;
    private String username;
    private String password;
    private String category;
}