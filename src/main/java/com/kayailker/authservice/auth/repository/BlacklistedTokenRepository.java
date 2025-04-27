package com.kayailker.authservice.auth.repository;

import com.kayailker.authservice.auth.model.entity.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, UUID> {
    Optional<BlacklistedToken> findByToken(String token);
    boolean existsByToken(String token);
}
