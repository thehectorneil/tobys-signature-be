package com.codewithneil.store.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.codewithneil.store.exceptions.InvalidCredentialsException;
import com.codewithneil.store.models.User;
import com.codewithneil.store.models.UserAccess;
import com.codewithneil.store.repositories.UserRepository;
import com.codewithneil.store.security.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // 🔹 CUSTOMER LOGIN
    public String customerLogin(String email, String password) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        if (!"CUSTOMER".equals(user.getRole().getName())) {
            throw new InvalidCredentialsException();
        }

        validatePassword(password, user.getPasswordHash());

        return jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().getName(),
                null // no access needed
        );
    }

    // 🔹 STAFF LOGIN (ADMIN + EMPLOYEE)
    public String staffLogin(String email, String password) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        String role = user.getRole().getName();

        if (!"ADMIN".equals(role) && !"EMPLOYEE".equals(role)) {
            throw new InvalidCredentialsException();
        }

        validatePassword(password, user.getPasswordHash());

        // 🔥 get system access
        List<String> accessList = user.getUserAccesses()
                .stream()
                .map(ua -> ua.getAccess().getCode())
                .collect(Collectors.toList());

        return jwtUtil.generateToken(
                user.getEmail(),
                role,
                accessList
        );
    }

    // 🔹 SHARED PASSWORD VALIDATION
    private void validatePassword(String raw, String hash) {
        if (!encoder.matches(raw, hash)) {
            throw new InvalidCredentialsException();
        }
    }
}