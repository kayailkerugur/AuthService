package com.kayailker.authservice.auth.service;

import com.kayailker.authservice.auth.exception.EmailAlreadyInUseException;
import com.kayailker.authservice.auth.exception.InvalidCredentialsException;
import com.kayailker.authservice.auth.exception.UsernameAlreadyInUseException;
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
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String accessToken = jwtUtil.generateToken(user.getEmail(), 86400000);
        String refreshToken = jwtUtil.generateToken(user.getEmail(), 2592000000L);

        return new JwtResponse(accessToken, refreshToken);
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
            throw new EmailAlreadyInUseException("Email already in use");
        }
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new UsernameAlreadyInUseException("Username already in use");
        }

        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setFullName(registerRequest.getFullName());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setVerified(false);

        userRepository.save(newUser);
    }
}
