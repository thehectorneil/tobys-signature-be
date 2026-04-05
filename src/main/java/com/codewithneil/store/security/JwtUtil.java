package com.codewithneil.store.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    // 🔹 Token validity (10 hours)
    private final long JWT_EXPIRATION = 1000 * 60 * 60 * 10;

    // 🔹 Generate signing key
    private Key getKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    // 🔹 GENERATE TOKEN
    public String generateToken(String email, String role, List<String> access) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("access", access);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 🔹 EXTRACT EMAIL (subject)
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    // 🔹 EXTRACT ROLE
    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    // 🔹 EXTRACT ACCESS LIST
    public List<String> extractAccess(String token) {
        return extractClaims(token).get("access", List.class);
    }

    // 🔹 CHECK TOKEN VALIDITY
    public boolean isTokenValid(String token, String email) {
        final String extractedEmail = extractEmail(token);
        return (extractedEmail.equals(email) && !isTokenExpired(token));
    }

    // 🔹 CHECK EXPIRATION
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // 🔹 PARSE CLAIMS
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}