package com.codewithneil.store.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /* =========================
       🔹 INVALID CREDENTIALS (CUSTOM)
    ========================= */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCredentials() {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                Map.of(
                        "status", "error",
                        "message", "Invalid email or password"
                )
        );
    }

    /* =========================
       🔹 RESPONSE STATUS EXCEPTION (NEW)
    ========================= */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleResponseStatus(ResponseStatusException ex) {

        return ResponseEntity.status(ex.getStatusCode()).body(
                Map.of(
                        "status", "error",
                        "message", ex.getReason() != null ? ex.getReason() : "Request failed"
                )
        );
    }

    /* =========================
       🔹 FALLBACK (ALL ERRORS)
    ========================= */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of(
                        "status", "error",
                        "message", "Internal server error"
                )
        );
    }
}