package com.kasv.gunda.bootcamp.controllers;

import com.google.gson.Gson;
import com.kasv.gunda.bootcamp.entities.User;
import com.kasv.gunda.bootcamp.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/existByUsername/{username}")
    public String byUsername(@PathVariable String username) {
        if (userRepository.existsByUsername(username)) {
            return "User with username " + username + " exists";
        } else {
            return "User with username " + username + " does not exist";
        }

    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {

        if (user.getUsername() == null || user.getPassword() == null) {
            return ResponseEntity.status(400).body("Username and password are required");
        }

        User userFromDb = userRepository.findByUsername(user.getUsername());

        if (userFromDb != null) {
            if (userFromDb.getPassword().equals(user.getPassword())) {
                return ResponseEntity.ok("Login successful");
            } else {
                return ResponseEntity.status(401).body("Invalid password");
            }
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }
}
