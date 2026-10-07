package com.example.todo.filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JWTTokenGeneratorFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Environment env = getEnvironment();

            if (env != null) {
                String secret = env.getProperty("jwt.secret");
                SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                String jwt = Jwts.builder().issuer("TodoApp")
                        .setSubject("Todo")
                        .claim("username", authentication.getName())
                        .claim("authorities", authentication.getAuthorities().stream().map(auth -> auth.getAuthority()).toList())
                        .issuedAt(new Date())
                        .expiration(new Date(System.currentTimeMillis() + 3600 * 1000)) // 1 hour expiration
                        .signWith(key)
                        .compact();

                response.setHeader("Authorization", "Bearer " + jwt);
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return super.shouldNotFilter(request);
    }
}
