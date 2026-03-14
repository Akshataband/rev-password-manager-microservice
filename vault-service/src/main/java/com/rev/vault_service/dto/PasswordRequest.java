
package com.rev.vault_service.dto;

import lombok.Data;
@Data
public class PasswordRequest {

    private String siteName;

    private String siteUrl;

    private String username;

    private String password;

    private String category;

    private String notes;

    private boolean favorite;
}