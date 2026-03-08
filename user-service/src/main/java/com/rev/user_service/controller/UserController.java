package com.rev.user_service.controller;


import com.rev.user_service.dto.LoginRequest;
import com.rev.user_service.dto.LoginResponse;
import com.rev.user_service.dto.RegisterUserRequest;
import com.rev.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public String registerUser(@Valid @RequestBody RegisterUserRequest request) {

        userService.registerUser(request);

        return "User registered successfully";
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        String token = userService.loginUser(request);

        return new LoginResponse(token);
    }
}