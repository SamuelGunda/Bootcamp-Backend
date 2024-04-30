package com.kasv.gunda.bootcamp.controllers;

import com.google.gson.Gson;
import com.kasv.gunda.bootcamp.entities.AuthCheck;
import com.kasv.gunda.bootcamp.entities.LoginRequest;
import com.kasv.gunda.bootcamp.entities.LogoutRequest;
import com.kasv.gunda.bootcamp.entities.User;
import com.kasv.gunda.bootcamp.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class AuthController {

    private final AuthService authService;
    public AuthController(AuthService authService) { this.authService = authService; }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {

        Gson gson = new Gson();
        Map<String, String> jsonResponse = new HashMap<>();
        System.out.println(loginRequest.getUsername() + " " + loginRequest.getPassword());
        if (loginRequest.getUsername() == null ||
                loginRequest.getPassword() == null ||
                loginRequest.getUsername().isEmpty() ||
                loginRequest.getPassword().isEmpty()) {

            jsonResponse.put("error", "Invalid request. Please provide valid input data.");

            return ResponseEntity.status(400).body(gson.toJson(jsonResponse));
        }

        return this.authService.login(loginRequest);
    }

    @PostMapping("/isAuthenticated")
    public ResponseEntity<String> isAuthenticated(@RequestBody AuthCheck authCheck) {

        Gson gson = new Gson();
        Map<String, String> jsonResponse = new HashMap<>();

        if (authCheck.getUsername() == null ||
                authCheck.getUsername().isEmpty() ||
                authCheck.getToken() == null ||
                authCheck.getToken().isEmpty()) {

            jsonResponse.put("error", "Invalid request. Please provide valid input data.");

            return ResponseEntity.status(400).body(gson.toJson(jsonResponse));
        }

        return this.authService.isAuthenticated(authCheck);
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

        return this.authService.logout(logoutRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {

        Gson gson = new Gson();
        Map<String, String> jsonResponse = new HashMap<>();

        if (user.getUsername() == null ||
                user.getUsername().isEmpty() ||
                user.getPassword() == null ||
                user.getPassword().isEmpty() ||
                user.getEmail() == null ||
                user.getEmail().isEmpty()) {

            jsonResponse.put("error", "Invalid request. Please provide valid input data.");

            return ResponseEntity.status(400).body(gson.toJson(jsonResponse));
        }

        return this.authService.register(user);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody User user) {

        Gson gson = new Gson();
        Map<String, String> jsonResponse = new HashMap<>();

        if (user.getUsername() == null ||
                user.getUsername().isEmpty() ||
                user.getEmail() == null ||
                user.getEmail().isEmpty()) {

            jsonResponse.put("error", "Invalid request. Please provide valid input data.");

            return ResponseEntity.status(400).body(gson.toJson(jsonResponse));
        }

        return this.authService.forgotPassword(user);
    }
}
