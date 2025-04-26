package com.kayailker.authservice.auth.service;

import com.kayailker.authservice.auth.exception.*;
import com.kayailker.authservice.auth.model.*;
import com.kayailker.authservice.auth.model.entity.User;
import com.kayailker.authservice.auth.repository.UserRepository;
import com.kayailker.authservice.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MailService mailService;

    public AuthServiceImpl(JwtUtil jwtUtil, PasswordEncoder passwordEncoder, UserRepository userRepository, MailService mailService) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.mailService = mailService;
    }

    @Override
    public JwtResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        if (Boolean.FALSE.equals(user.getVerified())) {
            throw new EmailNotVerifiedException("Email address is not verified. Please verify your email before logging in.");
        }

        String accessToken = jwtUtil.generateToken(user.getId(), user.getEmail(), 86400000);
        String refreshToken = jwtUtil.generateToken(user.getId(), user.getEmail(), 2592000000L);

        return new JwtResponse(accessToken, refreshToken);
    }

    @Override
    public JwtResponse refreshToken(String refreshToken) {
        if (jwtUtil.validateToken(refreshToken)) {
            String email = jwtUtil.getUsernameFromToken(refreshToken);
            UUID userId = UUID.fromString(jwtUtil.getUserIdFromToken(refreshToken));

            String newAccessToken = jwtUtil.generateToken(userId, email, 86400000);

            return new JwtResponse(newAccessToken, refreshToken);
        }
        throw new InvalidCredentialsException("Invalid refresh token");
    }

    @Override
    public void register(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new EmailAlreadyInUseException("Email already in use");
        }
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new UsernameAlreadyInUseException("Username already in use");
        }

        String verificationCode = generateVerificationCode();

        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setFullName(registerRequest.getFullName());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setVerified(false);
        newUser.setVerificationCode(verificationCode);

        userRepository.save(newUser);

        // Email Gönderiyoruz!
        mailService.sendEmail(
                newUser.getEmail(),
                "Email Doğrulama Kodu",
                "Merhaba " + newUser.getFullName() + ",\n\nEmail doğrulama kodunuz: " + verificationCode + "\n\nİyi günler dileriz!"
        );
    }

    @Override
    public UserProfileResponse getProfile(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userId = jwtUtil.getUserIdFromToken(token);

        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }

    @Override
    public String forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return jwtUtil.generateResetToken(user.getId());
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        if (!jwtUtil.validateToken(request.getResetToken())) {
            throw new InvalidResetTokenException("Invalid or expired reset token");
        }
        String userId = jwtUtil.getResetUserIdFromToken(request.getResetToken());

        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    private String generateVerificationCode() {
        int code = (int)(Math.random() * 900000) + 100000; // 6 haneli random sayı
        return String.valueOf(code);
    }

    @Override
    public void verifyEmail(VerifyEmailRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (Boolean.TRUE.equals(user.getVerified())) {
            throw new EmailNotVerifiedException("User already verified");
        }

        if (!request.getVerificationCode().equals(user.getVerificationCode())) {
            throw new InvalidVerificationCodeException("Invalid verification code");
        }

        user.setVerified(true);
        user.setVerificationCode(null); // Doğrulama kodu artık gerekmez
        userRepository.save(user);
    }

    @Override
    public void sendForgotPasswordCode(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (Boolean.FALSE.equals(user.getVerified())) {
            throw new EmailNotVerifiedException("Email address is not verified.");
        }

        String forgotPasswordCode = generateVerificationCode();
        user.setForgotPasswordCode(forgotPasswordCode);
        userRepository.save(user);

        mailService.sendEmail(
                user.getEmail(),
                "Şifre Sıfırlama Kodunuz",
                "Merhaba " + user.getFullName() + ",\n\nŞifre sıfırlama için doğrulama kodunuz: " + forgotPasswordCode + "\n\nBu kodu kullanarak şifrenizi sıfırlayabilirsiniz.\n\n- RestLocation Auth Team"
        );
    }

    @Override
    public void verifyForgotPasswordCode(ForgotPasswordCodeVerifyRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.getForgotPasswordCode() == null || !user.getForgotPasswordCode().equals(request.getCode())) {
            throw new InvalidVerificationCodeException("Invalid or expired forgot password code");
        }

        // Kod doğruysa ➔ bir daha kullanılmaması için sıfırla
        user.setForgotPasswordCode(null);
        userRepository.save(user);
    }
}
