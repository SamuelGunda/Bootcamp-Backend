package com.kasv.gunda.bootcamp.controllers;

import com.kasv.gunda.bootcamp.repositories.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
