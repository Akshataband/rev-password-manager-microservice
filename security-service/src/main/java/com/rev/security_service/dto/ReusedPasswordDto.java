package com.rev.security_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReusedPasswordDto {

    private String siteName;
    private String username;
}