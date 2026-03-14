package com.rev.vault_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResponse {

    private Long id;

    private String siteName;

    private String siteUrl;

    private String username;

    private String password;

    private String category;

    private String notes;

    private boolean favorite;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}