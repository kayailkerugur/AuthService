package com.kayailker.authservice.auth.repository;

import com.kayailker.authservice.auth.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    List<User> findAllByDeletionRequestedAtBefore(LocalDateTime dateTime);
}
