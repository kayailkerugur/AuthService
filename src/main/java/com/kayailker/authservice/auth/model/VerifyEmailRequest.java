package com.kayailker.authservice.auth.model;

import jakarta.validation.constraints.NotBlank;

public class VerifyEmailRequest {
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Verification code is required")
    private String verificationCode;

    public VerifyEmailRequest() {
    }

    public VerifyEmailRequest(String email, String verificationCode) {
        this.email = email;
        this.verificationCode = verificationCode;
    }

    public @NotBlank(message = "Email is required") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Email is required") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Verification code is required") String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(@NotBlank(message = "Verification code is required") String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
