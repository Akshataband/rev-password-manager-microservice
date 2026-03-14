package com.rev.user_service.service;

import com.rev.user_service.dto.*;

public interface UserService {

    void registerUser(RegisterUserRequest request);

    LoginResponse loginUser(LoginRequest request);

    void logout(String token);

    String refreshToken(String refreshToken);

    void changePassword(Long userId, ChangePasswordRequest request);

    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

    void updateProfile(Long userId, UpdateProfileRequest request);

    void deleteAccount(Long userId);
}