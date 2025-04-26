package com.kayailker.authservice.auth.model;

import jakarta.validation.constraints.NotBlank;

public class ResetForgetPasswordRequest {

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "New password is required")
    private String newPassword;

    public ResetForgetPasswordRequest() {
    }

    public ResetForgetPasswordRequest(String email, String newPassword) {
        this.email = email;
        this.newPassword = newPassword;
    }

    public @NotBlank(message = "Email is required") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Email is required") String email) {
        this.email = email;
    }

    public @NotBlank(message = "New password is required") String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(@NotBlank(message = "New password is required") String newPassword) {
        this.newPassword = newPassword;
    }
}
