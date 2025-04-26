package com.kayailker.authservice.auth.controller;

import com.kayailker.authservice.auth.model.JwtResponse;
import com.kayailker.authservice.auth.model.LoginRequest;
import com.kayailker.authservice.security.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        if ("test@example.com".equals(loginRequest.getEmail()) && "123456".equals(loginRequest.getPassword())) {
            String accessToken = jwtUtil.generateToken(loginRequest.getEmail(), 86400000);  // 1 gün
            String refreshToken = jwtUtil.generateToken(loginRequest.getEmail(), 2592000000L); // 30 gün
            return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken)); // <--- Dikkat
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
