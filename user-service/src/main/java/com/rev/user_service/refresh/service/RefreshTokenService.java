package com.rev.user_service.refresh.service;

import com.rev.user_service.refresh.entity.RefreshToken;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(Long userId);

    RefreshToken verifyExpiration(RefreshToken token);

}