package com.dev.auth.jwt;

import com.dev.admin.Admin;
import com.dev.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET =
            "9eee7fea57e114b48155cc9db96b66afd8ff827c023f3edc03d61659d96fcb22";

    /* USER TOKEN */
    public String generateUserToken(User user) {
        return buildToken(user.getEmail(), "USER", user.getName(), user.getPicture());
    }

    /* ADMIN TOKEN */
    public String generateAdminToken(Admin admin) {
        return buildToken(admin.getEmail(), "ADMIN", admin.getName(), null);
    }

    private String buildToken(String email, String role, String name, String picture) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("name", name);
        claims.put("picture", picture);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}
