package com.kasv.gunda.bootcamp.controllers;

import com.kasv.gunda.bootcamp.entities.LogoutRequest;
import com.kasv.gunda.bootcamp.entities.PasswordChangeRequest;
import com.kasv.gunda.bootcamp.entities.User;

import com.kasv.gunda.bootcamp.repositories.UserRepository;
import com.kasv.gunda.bootcamp.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.kasv.gunda.bootcamp.services.TokenService;


@RestController
//@RequestMapping("/api/users")
public class UserController {

    private TokenService tokenService = TokenService.getInstance();
    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/existByUsername/{username}")
    public String byUsername(@PathVariable String username) {
        if (userRepository.existsByUsername(username)) {
            return "User with username " + username + " exists";
        } else {
            return "User with username " + username + " does not exist";
        }
    }

    @PutMapping("/admin/password")
    public ResponseEntity<String> changePassword(@RequestParam String username ,@RequestBody PasswordChangeRequest request) {
        if (request.getToken() == null || request.getToken().isEmpty()
                || request.getNewPassword() == null || request.getNewPassword().isEmpty()
                || request.getOldPassword() == null || request.getOldPassword().isEmpty()) {

            return ResponseEntity.status(400).body("Invalid request. Please provide valid input data.");
        }

        if (!userRepository.existsByUsername(username)) {
            return ResponseEntity.status(404).body("User with username " + username + " not found.");
        }

        return userService.changePassword(request.getToken(), request.getNewPassword(), request.getOldPassword(),  username);
    }
}
