package com.kayailker.authservice.auth.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    @NotBlank(message = "Reset token is required")
    private String resetToken;

    @NotBlank(message = "New password is required")
    private String newPassword;

    public ResetPasswordRequest() {
    }

    public ResetPasswordRequest(String resetToken, String newPassword) {
        this.resetToken = resetToken;
        this.newPassword = newPassword;
    }

    public @NotBlank(message = "Reset token is required") String getResetToken() {
        return resetToken;
    }

    public void setResetToken(@NotBlank(message = "Reset token is required") String resetToken) {
        this.resetToken = resetToken;
    }

    public @NotBlank(message = "New password is required") String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(@NotBlank(message = "New password is required") String newPassword) {
        this.newPassword = newPassword;
    }
}