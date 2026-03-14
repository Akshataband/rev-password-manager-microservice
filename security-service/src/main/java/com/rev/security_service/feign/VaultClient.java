package com.rev.security_service.feign;


import com.rev.security_service.dto.PasswordDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "vault-service")
public interface VaultClient {

    @GetMapping("/api/passwords")
    List<PasswordDto> getAllPasswords();
}