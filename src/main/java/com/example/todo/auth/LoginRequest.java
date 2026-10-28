package com.example.todo.auth;

public record LoginRequest(
        String username,
        String password
) {
}
