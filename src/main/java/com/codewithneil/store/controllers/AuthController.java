package com.codewithneil.store.controllers;

import com.codewithneil.store.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> body) {

        String email = body.get("email");
        String password = body.get("password");

        String token = authService.login(email, password);

        return Map.of("token", token);
    }
}