package com.kayailker.authservice.auth.service;

import com.kayailker.authservice.auth.model.JwtResponse;
import com.kayailker.authservice.auth.model.LoginRequest;
import com.kayailker.authservice.auth.model.RegisterRequest;
import com.kayailker.authservice.auth.model.entity.User;
import com.kayailker.authservice.auth.repository.UserRepository;
import com.kayailker.authservice.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthServiceImpl(JwtUtil jwtUtil, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public JwtResponse login(LoginRequest loginRequest) {
        if ("test@example.com".equals(loginRequest.getEmail()) && "123456".equals(loginRequest.getPassword())) {
            String accessToken = jwtUtil.generateToken(loginRequest.getEmail(), 86400000);
            String refreshToken = jwtUtil.generateToken(loginRequest.getEmail(), 2592000000L);
            return new JwtResponse(accessToken, refreshToken);
        }
        throw new RuntimeException("Invalid credentials");
    }

    @Override
    public JwtResponse refreshToken(String refreshToken) {
        if (jwtUtil.validateToken(refreshToken)) {
            String username = jwtUtil.getUsernameFromToken(refreshToken);
            String newAccessToken = jwtUtil.generateToken(username, 86400000);
            return new JwtResponse(newAccessToken, refreshToken);
        }
        throw new RuntimeException("Invalid refresh token");
    }

    @Override
    public void register(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new RuntimeException("Username already in use");
        }

        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setFullName(registerRequest.getFullName());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword())); // 🔒 Şifreyi encode ettik
        newUser.setVerified(false);

        userRepository.save(newUser);
    }
}
