package com.kayailker.authservice.auth.model;

import java.time.Instant;
import java.util.UUID;

public class UserProfileResponse {
    private UUID id;
    private String username;
    private String fullName;
    private String email;
    private Instant createdAt;

    public UserProfileResponse() {
    }

    public UserProfileResponse(UUID id, String username, String fullName, String email, Instant createdAt) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
