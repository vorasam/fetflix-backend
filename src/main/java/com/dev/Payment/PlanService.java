package com.dev.Payment;

import com.dev.user.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PlanService {

    public boolean isPlanActive(User user) {

        return user.isPaymentDone()
                && user.getPlanExpiry() != null
                && user.getPlanExpiry().isAfter(LocalDateTime.now());
    }
}

