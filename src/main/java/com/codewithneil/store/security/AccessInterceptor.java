package com.codewithneil.store.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

@Component
public class AccessInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {

        // Only check controller methods
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod method = (HandlerMethod) handler;

        // 🔹 Check if annotation exists
        RequireAccess requireAccess = method.getMethodAnnotation(RequireAccess.class);

        if (requireAccess == null) {
            return true; // no restriction
        }

        String requiredAccess = requireAccess.value();

        // 🔹 Get token
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        String token = authHeader.substring(7);

        try {
            List<String> userAccess = jwtUtil.extractAccess(token);

            if (userAccess == null || !userAccess.contains(requiredAccess)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return false;
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        return true;
    }
}