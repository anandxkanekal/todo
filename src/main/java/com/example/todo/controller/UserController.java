package com.example.todo.controller;

import com.example.todo.dto.CreateUserRequest;
import com.example.todo.dto.UpdateUserRequest;
import com.example.todo.dto.UserResponse;
import com.example.todo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        UserResponse userResponse = userService.registerUser(createUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateUser(Authentication authentication, @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        UserResponse userResponse = userService.updateUser(authentication.getName(), updateUserRequest);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser(Authentication authentication) {
        userService.deleteUser(authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
