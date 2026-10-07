package com.example.todo.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.time.LocalDateTime;

public class BasicAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        LocalDateTime timestamp  = LocalDateTime.now();
        String message = authException != null ? authException.getMessage() : "Unauthorized";
        String path = request.getRequestURI();

        response.setHeader("error", "Unauthorized");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        String jsonResponse = String.format("{\"timestamp\":\"%s\",\"status\":%d,\"error\":\"Unauthorized\",\"message\":\"%s\",\"path\":\"%s\"}",
                timestamp, HttpStatus.UNAUTHORIZED.value(), message, path);
        response.getWriter().write(jsonResponse);
    }
}
