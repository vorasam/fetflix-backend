package com.dev.admin;

import com.dev.auth.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth/admin")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AdminLoginRequest req) {

        Optional<Admin> optionalAdmin = adminRepository.findByEmail(req.getEmail());

        if (optionalAdmin.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Admin email not found"));
        }

        Admin admin = optionalAdmin.get();

        if (!passwordEncoder.matches(req.getPassword(), admin.getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Incorrect password"));
        }

        String token = jwtService.generateAdminToken(admin);

        return ResponseEntity.ok(
                Map.of(
                        "token", token,
                        "role", "ADMIN"
                )
        );
    }

}
