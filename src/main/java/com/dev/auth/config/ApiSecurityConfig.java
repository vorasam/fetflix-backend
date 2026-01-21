package com.dev.auth.config;

import com.dev.auth.filter.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@Order(1) // 🔥 VERY IMPORTANT
public class ApiSecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final CorsConfig corsConfig;

    // ✅ Password encoder (used by admin login)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {

        http
                // 🔥 ONLY these paths
                .securityMatcher("/auth/admin/**", "/admin/api/**","/api/**")

                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)

                // 🔥 NEVER REDIRECT — JSON ONLY
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, e) -> {
                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.setContentType("application/json");
                            res.getWriter().write("{\"error\":\"Unauthorized\"}");
                        })
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/movies/**",
                                "/api/shows/**",
                                "/api/seasons/**",
                                "/api/episodes/**"
                        ).permitAll()
                        .requestMatchers("/auth/admin/login").permitAll()
                        .requestMatchers("/api/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/admin/api/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
