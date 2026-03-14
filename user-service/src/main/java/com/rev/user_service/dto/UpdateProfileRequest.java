package com.rev.user_service.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {

    private String name;
    private String email;

}