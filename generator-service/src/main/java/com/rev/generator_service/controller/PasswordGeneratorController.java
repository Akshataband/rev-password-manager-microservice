package com.rev.generator_service.controller;

import com.rev.generator_service.dto.PasswordGenerateRequest;
import com.rev.generator_service.dto.PasswordGenerateResponse;
import com.rev.generator_service.service.PasswordGeneratorService;
import lombok.RequiredArgsConstructor;import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/generator")
@RequiredArgsConstructor
public class PasswordGeneratorController {

    private final PasswordGeneratorService generatorService;

    @GetMapping("/generate")
    public PasswordGenerateResponse generate(
            @RequestParam(defaultValue = "12") int length,
            @RequestHeader("X-User-Email") String email) {

        PasswordGenerateRequest request = new PasswordGenerateRequest();
        request.setLength(length);

        List<String> passwords =
                generatorService.generatePasswords(request);

        return new PasswordGenerateResponse(passwords);
    }
}