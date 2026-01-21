package com.dev.auth.filter;

import com.dev.admin.Admin;
import com.dev.admin.AdminRepository;
import com.dev.auth.jwt.JwtService;
import com.dev.user.User;
import com.dev.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        return path.startsWith("/oauth2/")
                || path.startsWith("/login/")
                || path.startsWith("/auth/admin/login")
                || path.equals("/");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);
        String role = jwtService.extractRole(token);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            if ("ADMIN".equals(role)) {

                Admin admin = adminRepository.findByEmail(email).orElseThrow();

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                admin, null, List.of(() -> "ROLE_ADMIN")
                        );

                SecurityContextHolder.getContext().setAuthentication(auth);
            }

            if ("USER".equals(role)) {

                User user = userRepository.findByEmail(email).orElseThrow();

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                user, null, List.of(() -> "ROLE_USER")
                        );

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}
