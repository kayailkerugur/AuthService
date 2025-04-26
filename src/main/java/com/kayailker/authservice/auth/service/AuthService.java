package com.kayailker.authservice.auth.service;

import com.kayailker.authservice.auth.model.*;

public interface AuthService {
    JwtResponse login(LoginRequest loginRequest);
    JwtResponse refreshToken(String refreshToken);
    void register(RegisterRequest registerRequest);
    UserProfileResponse getProfile(String authHeader);

    String forgotPassword(ForgotPasswordRequest request);
    void resetPassword(ResetPasswordRequest request);

    void verifyEmail(VerifyEmailRequest request);

    void sendForgotPasswordCode(ForgotPasswordRequest request);
    void verifyForgotPasswordCode(ForgotPasswordCodeVerifyRequest request);
}
