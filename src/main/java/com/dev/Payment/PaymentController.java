package com.dev.Payment;

import com.dev.user.User;
import com.dev.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final RazorpayService razorpayService;
    private final UserRepository userRepo;

    // CREATE ORDER
    @PostMapping("/create-order")
    public String createOrder(@RequestParam PlanType plan) throws Exception {

        int amount = switch (plan) {
            case BASIC -> 1;      // ₹1
            case SILVER -> 2;     // ₹2
            case GOLD -> 3;       // ₹3
            case PLATINUM -> 4;   // ₹4
        };

        return razorpayService.createOrder(amount).toString();
    }

    // VERIFY PAYMENT
    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(
            @RequestBody Map<String, String> data,
            Authentication auth) {

        boolean valid = razorpayService.verify(
                data.get("razorpay_order_id"),
                data.get("razorpay_payment_id"),
                data.get("razorpay_signature")
        );

        if (!valid) {
            return ResponseEntity.badRequest().body("Payment failed");
        }

        User user = userRepo.findByEmail(auth.getName()).orElseThrow();

        user.setPaymentDone(true);
        user.setPlan(PlanType.valueOf(data.get("plan")));
        user.setPlanExpiry(LocalDateTime.now().plusMonths(1)); // ✅ 1 MONTH

        userRepo.save(user);

        return ResponseEntity.ok("Payment successful");
    }

    @GetMapping("/status")
    public Map<String, Object> paymentStatus(Authentication auth) {

        if (auth == null) {
            return Map.of("paid", false);
        }

        User user = userRepo.findByEmail(auth.getName())
                .orElseThrow();

        boolean active = user.isPaymentDone()
                && user.getPlanExpiry() != null
                && user.getPlanExpiry().isAfter(LocalDateTime.now());

        return Map.of(
                "paid", active,
                "plan", user.getPlan(),
                "expiry", user.getPlanExpiry()
        );
    }

}

