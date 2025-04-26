package com.kayailker.authservice.auth.model;

import jakarta.validation.constraints.NotBlank;

public class ForgotPasswordCodeVerifyRequest {

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Verification code is required")
    private String code;

    public ForgotPasswordCodeVerifyRequest() {
    }

    public ForgotPasswordCodeVerifyRequest(String email, String code) {
        this.email = email;
        this.code = code;
    }

    public @NotBlank(message = "Email is required") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Email is required") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Verification code is required") String getCode() {
        return code;
    }

    public void setCode(@NotBlank(message = "Verification code is required") String code) {
        this.code = code;
    }
}
