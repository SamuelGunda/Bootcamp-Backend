package com.kasv.gunda.bootcamp.controllers;

import com.kasv.gunda.bootcamp.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;
import com.kasv.gunda.bootcamp.services.TokenService;


@RestController
//@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private TokenService tokenService = TokenService.getInstance();

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
