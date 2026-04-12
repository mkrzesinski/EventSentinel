package com.portfolio.userservice.service;

import com.portfolio.userservice.model.User;
import com.portfolio.userservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        log.info("Creating user: username={}, email={}", user.getUsername(), user.getEmail());
        User saved = userRepository.save(user);
        log.info("User created: id={}", saved.getId());
        return saved;
    }

    public User getUser(Long id) {
        log.info("Fetching user: id={}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found: id={}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + id);
                });
    }

    public boolean validateUser(Long id) {
        boolean exists = userRepository.existsById(id);
        log.info("Validating user: id={}, exists={}", id, exists);
        return exists;
    }
}
