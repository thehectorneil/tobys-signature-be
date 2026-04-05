package com.codewithneil.store.controllers;

import com.codewithneil.store.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.codewithneil.store.dto.LoginRequest;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // 🔹 CUSTOMER LOGIN
    @PostMapping("/customer/login")
    public Map<String, String> customerLogin(@RequestBody LoginRequest request) {

        String token = authService.customerLogin(
                request.getEmail(),
                request.getPassword()
        );

        return Map.of(
                "status", "success",
                "type", "customer",
                "token", token
        );
    }

    // 🔹 STAFF LOGIN (ADMIN + EMPLOYEE)
    @PostMapping("/staff/login")
    public Map<String, String> staffLogin(@RequestBody LoginRequest request) {

        String token = authService.staffLogin(
                request.getEmail(),
                request.getPassword()
        );

        return Map.of(
                "status", "success",
                "type", "staff",
                "token", token
        );
    }
}