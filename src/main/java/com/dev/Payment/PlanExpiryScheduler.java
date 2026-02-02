package com.dev.Payment;

import com.dev.user.User;
import com.dev.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@EnableScheduling
@Component
@RequiredArgsConstructor
public class PlanExpiryScheduler {

    private final UserRepository userRepo;

    @Scheduled(cron = "0 0 0 * * ?") // daily midnight
    public void expirePlans() {

        List<User> users = userRepo.findAll();

        for (User user : users) {
            if (user.getPlanExpiry() != null &&
                    user.getPlanExpiry().isBefore(LocalDateTime.now())) {

                user.setPaymentDone(false);
                userRepo.save(user);
            }
        }
    }
}

