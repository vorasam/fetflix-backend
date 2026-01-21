package com.dev.auth.config;

import com.dev.auth.handler.OAuth2SuccessHandler;
import com.dev.auth.service.OAuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@Order(2)
public class OAuthSecurityConfig {

    private final OAuthUserService oAuthUserService;
    private final OAuth2SuccessHandler successHandler;
    private final CorsConfig corsConfig;

    @Bean
    public SecurityFilterChain oauthSecurity(HttpSecurity http) throws Exception {

        http
                // 🔥 ONLY Google login endpoints
                .securityMatcher("/login/**", "/oauth2/**")

                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()

                )

                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(userInfo ->
                                userInfo.userService(oAuthUserService)
                        )
                        .successHandler(successHandler)
                );

        return http.build();
    }
}
