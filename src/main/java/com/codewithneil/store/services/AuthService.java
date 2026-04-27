package com.codewithneil.store.services;

import java.util.List;
import java.util.stream.Collectors;

import com.codewithneil.store.dto.AuthResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.codewithneil.store.exceptions.InvalidCredentialsException;
import com.codewithneil.store.models.User;
import com.codewithneil.store.repositories.UserRepository;
import com.codewithneil.store.security.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /* =========================
       CUSTOMER LOGIN
    ========================= */
    public AuthResponse customerLogin(String email, String password) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        String role = user.getRole().getName();

        if (!"CUSTOMER".equals(role)) {
            throw new InvalidCredentialsException();
        }

        validatePassword(password, user.getPasswordHash());

        String token = jwtUtil.generateToken(
                user.getEmail(),
                role,
                null // no access needed
        );

        return new AuthResponse(token, role); // 🔥 RETURN ROLE
    }

    /* =========================
       STAFF LOGIN (ADMIN + STAFF)
    ========================= */
    public AuthResponse staffLogin(String email, String password) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        String role = user.getRole().getName();

        if (!"ADMIN".equals(role) && !"STAFF".equals(role)) {
            throw new InvalidCredentialsException();
        }

        validatePassword(password, user.getPasswordHash());

        // 🔥 SYSTEM ACCESS
        List<String> accessList = user.getUserAccesses()
                .stream()
                .map(ua -> ua.getAccess().getCode())
                .collect(Collectors.toList());

        String token = jwtUtil.generateToken(
                user.getEmail(),
                role,
                accessList
        );

        return new AuthResponse(token, role); // 🔥 RETURN ROLE
    }

    /* =========================
       PASSWORD VALIDATION
    ========================= */
    private void validatePassword(String raw, String hash) {
        if (!encoder.matches(raw, hash)) {
            throw new InvalidCredentialsException();
        }
    }
}