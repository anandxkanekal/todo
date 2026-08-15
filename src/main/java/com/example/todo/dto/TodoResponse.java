package com.example.todo.dto;

import com.example.todo.constant.TodoStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TodoResponse {

    private Long id;

    private String title;

    private String description;

    private LocalDateTime dueDate;

    private TodoStatus status;
}
