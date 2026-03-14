package com.rev.security_service.service;

import com.rev.security_service.client.VaultClient;
import com.rev.security_service.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SecurityAuditService {

    private final VaultClient vaultClient;
    private final PasswordStrengthService strengthService;

    public SecurityAuditResponse audit() {

        List<PasswordDto> passwords = vaultClient.getAllPasswords();

        int weak = 0;

        Map<String, Integer> reuseMap = new HashMap<>();

        for (PasswordDto p : passwords) {

            if (strengthService.analyze(p.getPassword()).getStrength().equals("Weak")) {
                weak++;
            }

            reuseMap.put(
                    p.getPassword(),
                    reuseMap.getOrDefault(p.getPassword(), 0) + 1
            );
        }

        int reused = (int) reuseMap.values()
                .stream()
                .filter(v -> v > 1)
                .count();

        return new SecurityAuditResponse(
                passwords.size(),
                weak,
                reused
        );
    }

    public List<WeakPasswordDto> weakPasswords() {

        List<PasswordDto> passwords = vaultClient.getAllPasswords();

        return passwords.stream()
                .filter(p ->
                        strengthService
                                .analyze(p.getPassword())
                                .getStrength()
                                .equals("Weak")
                )
                .map(p -> new WeakPasswordDto(
                        p.getSiteName(),
                        p.getUsername()
                ))
                .collect(Collectors.toList());
    }

    public List<ReusedPasswordDto> reusedPasswords() {

        List<PasswordDto> passwords = vaultClient.getAllPasswords();

        Map<String, List<PasswordDto>> grouped =
                passwords.stream()
                        .collect(Collectors.groupingBy(
                                PasswordDto::getPassword
                        ));

        return grouped.values()
                .stream()
                .filter(list -> list.size() > 1)
                .flatMap(List::stream)
                .map(p -> new ReusedPasswordDto(
                        p.getSiteName(),
                        p.getUsername()
                ))
                .collect(Collectors.toList());
    }
}