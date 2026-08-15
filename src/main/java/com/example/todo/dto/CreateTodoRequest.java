package com.example.todo.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateTodoRequest {

    @NotBlank(message = "Title is mandatory")
    @Size(max = 100, message = "Title must be less than or equal to 100 characters")
    private String title;

    @Size(max = 500, message = "Description must be less than or equal to 500 characters")
    private String description;

    @Future(message = "Due date must be in the future")
    private LocalDateTime dueDate;
}
