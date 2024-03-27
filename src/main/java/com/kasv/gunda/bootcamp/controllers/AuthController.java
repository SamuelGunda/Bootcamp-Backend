package com.kasv.gunda.bootcamp.controllers;

import com.google.gson.Gson;
import com.kasv.gunda.bootcamp.entities.LogoutRequest;
import com.kasv.gunda.bootcamp.entities.User;
import com.kasv.gunda.bootcamp.repositories.UserRepository;
import com.kasv.gunda.bootcamp.services.TokenService;
import com.kasv.gunda.bootcamp.utilities.TokenGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
//@RequestMapping("/api")
public class AuthController {

    private final UserRepository userRepository;

    private TokenService tokenService = TokenService.getInstance();

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {

        Gson gson = new Gson();
        Map<String, String> jsonResponse = new HashMap<>();

        if (user.getUsername() == null ||
                user.getPassword() == null ||
                user.getUsername().isEmpty() ||
                user.getPassword().isEmpty()) {

            jsonResponse.put("error", "Invalid request. Please provide valid input data.");

            return ResponseEntity.status(400).body(gson.toJson(jsonResponse));
        }

        User userFromDb = userRepository.findByUsername(user.getUsername());

        if (userFromDb != null) {
            if (userFromDb.getPassword().equals(user.getPassword())) {
                TokenGenerator tokenGenerator = new TokenGenerator();

                String token = tokenGenerator.generateToken();
                tokenService.storeToken((long) userFromDb.getId(), token);

//                jsonResponse.put("token", "Bearer " + token);
                jsonResponse.put("token", token);
                jsonResponse.put("username", userFromDb.getUsername());
                return ResponseEntity.status(200).body(gson.toJson(jsonResponse));

            } else {

                jsonResponse.put("error", "Invalid credentials. Please provide valid credentials.");

                return ResponseEntity.status(401).body(gson.toJson(jsonResponse));
            }
        } else {
            jsonResponse.put("error", "User with username " + user.getUsername() + " does not exist. Please provide valid credentials.");

            return ResponseEntity.status(404).body(gson.toJson(jsonResponse));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutRequest logoutRequest) {

        Gson gson = new Gson();
        Map<String, String> jsonResponse = new HashMap<>();

        if (logoutRequest.getUsername() == null ||
                logoutRequest.getToken() == null ||
                logoutRequest.getUsername().isEmpty() ||
                logoutRequest.getToken().isEmpty()) {

            jsonResponse.put("error", "Invalid request. Please provide valid input data.");

            return ResponseEntity.status(400).body(gson.toJson(jsonResponse));
        }

        long userId = (long) userRepository.findIdByUsername(logoutRequest.getUsername());

        if (tokenService.isTokenExists(userId)) {
            if (tokenService.isTokenValid(logoutRequest.getToken())) {
                tokenService.removeToken(userId);
                jsonResponse.put("message", "User logged out successfully");
                return ResponseEntity.status(200).body("{}");
            } else {
                jsonResponse.put("error", "Invalid token. Please provide valid token.");
                return ResponseEntity.status(401).body(gson.toJson(jsonResponse));
            }
        } else {
            jsonResponse.put("error", "User  " + logoutRequest.getUsername() + " is not logged in.");
            return ResponseEntity.status(404).body(gson.toJson(jsonResponse));
        }

    }
}
