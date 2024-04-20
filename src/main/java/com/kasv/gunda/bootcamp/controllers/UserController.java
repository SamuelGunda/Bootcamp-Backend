package com.kasv.gunda.bootcamp.controllers;

import com.google.gson.Gson;
import com.kasv.gunda.bootcamp.entities.UpdatePassword;

import com.kasv.gunda.bootcamp.repositories.UserRepository;
import com.kasv.gunda.bootcamp.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.kasv.gunda.bootcamp.services.TokenService;

import java.util.HashMap;
import java.util.Map;


@RestController
//@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/exist/{username}")
    public ResponseEntity<String> byUsername(@PathVariable String username) {

        Gson gson = new Gson();
        Map<String, String> jsonResponse = new HashMap<>();

        if (userRepository.existsByUsername(username)) {
            jsonResponse.put("message", "User with username " + username + " exists");
            return ResponseEntity.status(200).body(gson.toJson(jsonResponse));
        } else {
            jsonResponse.put("message", "User with username " + username + " does not exist");
            return ResponseEntity.status(404).body(gson.toJson(jsonResponse));
        }
    }

    @PutMapping("/admin/password")
    public ResponseEntity<String> changePassword(@RequestBody UpdatePassword request) {

        Gson gson = new Gson();
        Map<String, String> jsonResponse = new HashMap<>();

        if (request.getToken() == null || request.getToken().isEmpty()
                || request.getNewPassword() == null || request.getNewPassword().isEmpty()
                || request.getOldPassword() == null || request.getOldPassword().isEmpty()
                || request.getUsername() == null || request.getUsername().isEmpty()) {

            jsonResponse.put("error", "Invalid request. Please provide valid input data.");
            return ResponseEntity.status(400).body(gson.toJson(jsonResponse));
        }

        return userService.changePassword(request);
    }
}
