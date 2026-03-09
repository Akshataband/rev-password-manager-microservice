package com.rev.password_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResponse {

    private Long id;

    private String siteName;

    private String username;

    private String password;

}