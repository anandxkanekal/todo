package com.example.todo.service;

import com.example.todo.dto.CreateUserRequest;
import com.example.todo.dto.UpdateUserRequest;
import com.example.todo.dto.UserResponse;
import com.example.todo.exception.UserAlreadyExistsException;
import com.example.todo.model.User;
import com.example.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserResponse registerUser(CreateUserRequest createUserRequest) {
        // Check if the username or email already exists
        if (userRepository.findByUsername(createUserRequest.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        if (userRepository.findByEmail(createUserRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        // Create a new User entity and save it to the database
        User user = new User();
        user.setUsername(createUserRequest.getUsername());
        user.setEmail(createUserRequest.getEmail());
        user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        user.setEnabled(true);
        user.setRole("ROLE_USER"); // Default role

        User savedUser = userRepository.save(user);

        // Create a UserResponse DTO to return
        UserResponse userResponse = new UserResponse();
        userResponse.setId(savedUser.getId());
        userResponse.setUsername(savedUser.getUsername());
        userResponse.setEmail(savedUser.getEmail());

        return userResponse;
    }

    public UserResponse updateUser(String username, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserAlreadyExistsException("User not found"));

        // Update user fields
        user.setEmail(updateUserRequest.getEmail());
        user.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));

        User updatedUser = userRepository.save(user);

        // Create a UserResponse DTO to return
        UserResponse userResponse = new UserResponse();
        userResponse.setId(updatedUser.getId());
        userResponse.setUsername(updatedUser.getUsername());
        userResponse.setEmail(updatedUser.getEmail());

        return userResponse;
    }

    public void deleteUser(String name) {

    }
}
