package com.rev.user_service.controller;

import com.rev.user_service.dto.*;
import com.rev.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // REGISTER
    @PostMapping("/register")
    public String register(@RequestBody RegisterUserRequest request) {

        userService.registerUser(request);
        return "User registered successfully";
    }

//    // LOGIN
//    @PostMapping("/login")
//    public LoginResponse login(@RequestBody LoginRequest request) {
//
//        return userService.loginUser(request);
//    }

    // LOGOUT
    @PostMapping("/logout")
    public String logout(@RequestHeader("Authorization") String token) {

        userService.logout(token);
        return "Logged out successfully";
    }

    // REFRESH TOKEN
    @PostMapping("/refresh-token")
    public String refreshToken(@RequestBody RefreshTokenRequest request) {

        return userService.refreshToken(request.getRefreshToken());
    }

    // CHANGE PASSWORD
    @PostMapping("/change-password")
    public String changePassword(
            @RequestParam Long userId,
            @RequestBody ChangePasswordRequest request) {

        userService.changePassword(userId, request);
        return "Password changed successfully";
    }

    // FORGOT PASSWORD
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody ForgotPasswordRequest request) {

        userService.forgotPassword(request);
        return "Password reset request sent";
    }

    // RESET PASSWORD
    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody ResetPasswordRequest request) {

        userService.resetPassword(request);
        return "Password reset successful";
    }

    // UPDATE PROFILE
    @PutMapping("/profile")
    public String updateProfile(
            @RequestParam Long userId,
            @RequestBody UpdateProfileRequest request) {

        userService.updateProfile(userId, request);
        return "Profile updated";
    }

    // DELETE ACCOUNT
    @DeleteMapping("/account")
    public String deleteAccount(@RequestParam Long userId) {

        userService.deleteAccount(userId);
        return "Account deleted";
    }
}