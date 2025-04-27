package com.kayailker.authservice.auth.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "blacklisted_tokens")
public class BlacklistedToken {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 500)
    private String token;

    @CreationTimestamp
    @Column(name = "blacklisted_at", nullable = false, updatable = false)
    private LocalDateTime blacklistedAt;

    public BlacklistedToken(UUID id, String token, LocalDateTime blacklistedAt) {
        this.id = id;
        this.token = token;
        this.blacklistedAt = blacklistedAt;
    }

    public BlacklistedToken() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getBlacklistedAt() {
        return blacklistedAt;
    }

    public void setBlacklistedAt(LocalDateTime blacklistedAt) {
        this.blacklistedAt = blacklistedAt;
    }
}
