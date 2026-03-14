package com.rev.vault_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpiringPasswordResponse {

    private Long id;
    private String siteName;
    private int daysOld;
}