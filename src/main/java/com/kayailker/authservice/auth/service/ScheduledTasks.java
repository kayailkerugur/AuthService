package com.kayailker.authservice.auth.service;


import com.kayailker.authservice.auth.model.entity.User;
import com.kayailker.authservice.auth.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ScheduledTasks {

    private final UserRepository userRepository;

    public ScheduledTasks(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void permanentlyDeleteExpiredAccounts() {
        List<User> users = userRepository.findAllByDeletionRequestedAtBefore(LocalDateTime.now().minusDays(30));

        if (!users.isEmpty()) {
            userRepository.deleteAll(users);
            System.out.println(users.size() + " kullanıcı kalıcı olarak silindi.");
        }
    }
}
