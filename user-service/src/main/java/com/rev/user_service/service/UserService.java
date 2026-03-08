package com.rev.user_service.service;

import com.rev.user_service.dto.LoginRequest;
import com.rev.user_service.dto.RegisterUserRequest;

public interface UserService {

    void registerUser(RegisterUserRequest request);

    String loginUser(LoginRequest request);

}