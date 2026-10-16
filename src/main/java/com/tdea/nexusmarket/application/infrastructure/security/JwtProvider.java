package com.tdea.nexusmarket.application.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.Key;

@Component
public class JwtProvider {

    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long expiration = 1000L * 60 * 60 * 10; // 10 horas

    public String generateToken(String username, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);
        return Jwts.builder()
            .subject(username)
            .claim("role", role)
            .issuedAt(now)
            .expiration(expiry)
            .signWith(secretKey)
            .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    public String getRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    public Claims getClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    public Key getSecretKey() {
        return secretKey;
    }
}
