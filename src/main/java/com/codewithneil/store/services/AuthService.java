package com.codewithneil.store.services;

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

    public String login(String email, String password) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        if (!encoder.matches(password, user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        return jwtUtil.generateToken(email);
    }

}