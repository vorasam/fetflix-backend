package com.dev.auth.service;

import com.dev.user.Role;
import com.dev.user.User;
import com.dev.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthUserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) {

        User user2 = new User();
        OAuth2User oauth2User = super.loadUser(request);

        String email = oauth2User.getAttribute("email");
        String picture = oauth2User.getAttribute("picture"); // ✅ GOOGLE IMAGE URL


        userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User user = User.builder()
                            .email(email)
                            .name(oauth2User.getAttribute("name"))
                            .role(Role.USER)
                            .provider("GOOGLE")
                            .picture(picture)
                            .enabled(user2.isEnabled())
                            .build();
                    return userRepository.save(user);
                });

        return oauth2User;
    }
}
