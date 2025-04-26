package com.kayailker.authservice.auth.service;

import com.kayailker.authservice.auth.model.JwtResponse;
import com.kayailker.authservice.auth.model.LoginRequest;
import com.kayailker.authservice.auth.model.RegisterRequest;
import com.kayailker.authservice.auth.model.UserProfileResponse;

public interface AuthService {
    JwtResponse login(LoginRequest loginRequest);
    JwtResponse refreshToken(String refreshToken);
    void register(RegisterRequest registerRequest);
    UserProfileResponse getProfile(String authHeader);
}
