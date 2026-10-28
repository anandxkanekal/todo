package com.example.todo.service;

import com.example.todo.constant.TodoStatus;
import com.example.todo.dto.CreateTodoRequest;
import com.example.todo.dto.TodoResponse;
import com.example.todo.exception.TodoNotFoundException;
import com.example.todo.model.Todo;
import com.example.todo.model.User;
import com.example.todo.repository.TodoRepository;
import com.example.todo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final UserRepository userRepository;

    private final TodoRepository todoRepository;

    @Transactional
    public TodoResponse createTodo(CreateTodoRequest createTodoRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Todo todo = new Todo();
        todo.setTitle(createTodoRequest.getTitle());
        todo.setDescription(createTodoRequest.getDescription());
        todo.setDueDate(createTodoRequest.getDueDate());
        todo.setStatus(TodoStatus.NEW);
        todo.setUser(user);
        Todo savedTodo = todoRepository.save(todo);

        TodoResponse todoResponse = new TodoResponse();
        todoResponse.setId(savedTodo.getId());
        todoResponse.setTitle(savedTodo.getTitle());
        todoResponse.setDescription(savedTodo.getDescription());
        todoResponse.setDueDate(savedTodo.getDueDate());
        todoResponse.setStatus(savedTodo.getStatus());

        return todoResponse;
    }

    public TodoResponse getTodoById(Long id) {
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new TodoNotFoundException(id));
        TodoResponse todoResponse = new TodoResponse();
        todoResponse.setId(todo.getId());
        todoResponse.setTitle(todo.getTitle());
        todoResponse.setDescription(todo.getDescription());
        todoResponse.setDueDate(todo.getDueDate());
        todoResponse.setStatus(todo.getStatus());
        return todoResponse;
    }

    @Transactional
    public void updateTodo(Long id, Todo todo) {
        Todo savedTodo = todoRepository.findById(id).orElseThrow(() -> new TodoNotFoundException(id));
        savedTodo.setTitle(todo.getTitle());
        savedTodo.setDescription(todo.getDescription());
        savedTodo.setDueDate(todo.getDueDate());
        savedTodo.setStatus(todo.getStatus());
        todoRepository.save(savedTodo);
    }

    @Transactional
    public void deleteTodoById(Long id) {
        Todo existingTodo = todoRepository.findById(id).orElseThrow(() -> new TodoNotFoundException(id));
        todoRepository.delete(existingTodo);
    }
}