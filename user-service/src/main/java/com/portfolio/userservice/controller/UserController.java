package com.portfolio.userservice.controller;

import com.portfolio.userservice.model.User;
import com.portfolio.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("service", "user-service", "status", "UP");
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @GetMapping("/users/{id}/validate")
    public ResponseEntity<Void> validateUser(@PathVariable Long id) {
        return userService.validateUser(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}