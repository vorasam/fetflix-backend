package com.dev.auth.handler;

import com.dev.auth.jwt.JwtService;
import com.dev.user.Role;
import com.dev.user.User;
import com.dev.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .email(email)
                                .name(oauthUser.getAttribute("name"))
                                .picture(oauthUser.getAttribute("picture")) // 👈 ADD THIS
                                .role(Role.USER)
                                .provider("GOOGLE")
                                .enabled(true)
                                .build()
                ));



        String token = jwtService.generateUserToken(user);

        // 🔥 PRINT TOKEN IN CONSOLE
        System.out.println("===== JWT TOKEN GENERATED =====");
        System.out.println(token);
        System.out.println("================================");

        // ✅ Redirect back to Angular with token
        response.sendRedirect(
                "http://localhost:4200/oauth-success?token=" + token
        );
    }
}
