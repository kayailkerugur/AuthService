package com.kayailker.authservice.auth.controller;

import com.kayailker.authservice.auth.model.*;
import com.kayailker.authservice.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        JwtResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshToken(@RequestBody RefreshTokenRequest refreshToken) {
        JwtResponse response = authService.refreshToken(refreshToken.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Kayıt başarılı! Lütfen emailinizi kontrol ederek hesabınızı doğrulayın."));    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getProfile(@RequestHeader("Authorization") String authHeader) {
        UserProfileResponse profile = authService.getProfile(authHeader);
        return ResponseEntity.ok(profile);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        String resetToken = authService.forgotPassword(request);

        Map<String, String> response = new HashMap<>();
        response.put("resetToken", resetToken);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        authService.resetPassword(request);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Password reset successfully");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<Map<String, String>> verifyEmail(@RequestBody @Valid VerifyEmailRequest request) {
        authService.verifyEmail(request);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Email successfully verified! Now you can log in.");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password/request-code")
    public ResponseEntity<Map<String, String>> sendForgotPasswordCode(@RequestBody @Valid ForgotPasswordRequest request) {
        authService.sendForgotPasswordCode(request);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Şifre sıfırlama kodu e-posta adresinize gönderildi.");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password/verify-code")
    public ResponseEntity<Map<String, String>> verifyForgotPasswordCode(@RequestBody @Valid ForgotPasswordCodeVerifyRequest request) {
        authService.verifyForgotPasswordCode(request);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Doğrulama kodu doğru! Şifre sıfırlama adımına geçebilirsiniz.");

        return ResponseEntity.ok(response);
    }
}
