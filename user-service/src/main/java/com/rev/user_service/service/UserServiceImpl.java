package com.rev.user_service.service;

import com.rev.user_service.dto.*;
import com.rev.user_service.entity.User;
import com.rev.user_service.refresh.entity.RefreshToken;
import com.rev.user_service.refresh.repository.RefreshTokenRepository;
import com.rev.user_service.refresh.service.RefreshTokenService;
import com.rev.user_service.repository.UserRepository;
import com.rev.user_service.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    // REGISTER
    @Override
    public void registerUser(RegisterUserRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);
    }

    // LOGIN
    @Override
    public LoginResponse loginUser(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String accessToken = jwtUtil.generateToken(user.getEmail());

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user.getId());

        return new LoginResponse(accessToken, refreshToken.getToken());
    }

    // LOGOUT
    @Override
    public void logout(String token) {

        // token blacklist logic can be added later
        System.out.println("User logged out");
    }

    // REFRESH TOKEN
    @Override
    public String refreshToken(String requestToken) {

        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(requestToken)
                .orElseThrow(() -> new RuntimeException("Token not found"));

        refreshTokenService.verifyExpiration(refreshToken);

        return jwtUtil.generateToken(refreshToken.getUser().getEmail());
    }

    // CHANGE PASSWORD
    @Override
    public void changePassword(Long userId, ChangePasswordRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);
    }

    // FORGOT PASSWORD
    @Override
    public void forgotPassword(ForgotPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // In production generate reset token and send email
        System.out.println("Password reset requested for " + user.getEmail());
    }

    // RESET PASSWORD
    @Override
    public void resetPassword(ResetPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);
    }

    // UPDATE PROFILE
    @Override
    public void updateProfile(Long userId, UpdateProfileRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        userRepository.save(user);
    }

    // DELETE ACCOUNT
    @Override
    public void deleteAccount(Long userId) {

        userRepository.deleteById(userId);
    }
}