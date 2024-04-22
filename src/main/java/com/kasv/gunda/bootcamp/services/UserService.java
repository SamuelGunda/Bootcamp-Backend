package com.kasv.gunda.bootcamp.services;

import com.google.gson.Gson;
import com.kasv.gunda.bootcamp.controllers.AuthController;
import com.kasv.gunda.bootcamp.entities.LogoutRequest;
import com.kasv.gunda.bootcamp.entities.UpdatePassword;
import com.kasv.gunda.bootcamp.entities.User;
import com.kasv.gunda.bootcamp.repositories.StudentRepository;
import com.kasv.gunda.bootcamp.repositories.UserRepository;
import com.kasv.gunda.bootcamp.utilities.TokenFunctions;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final StudentRepository studentRepository;
    private final AuthController authController;

    private TokenFunctions tokenFunctions = TokenFunctions.getInstance();

    public UserService(UserRepository userRepository, StudentRepository studentRepository, AuthController authController) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.authController = authController;
    }

    public ResponseEntity<String> changePassword(UpdatePassword request) {

        Gson gson = new Gson();
        Map<String, String> jsonResponse = new HashMap<>();

        if (!userRepository.existsByUsername(request.getUsername())) {
            jsonResponse.put("error", "User with username " + request.getUsername() + " does not exist.");
            return ResponseEntity.status(404).body(gson.toJson(jsonResponse));

        }

        if (!tokenFunctions.isTokenValid(request.getToken())) {
            jsonResponse.put("error", "Invalid token. Please provide a valid token.");
            return ResponseEntity.status(401).body(gson.toJson(jsonResponse));
        }

        User userFromDb = userRepository.findByUsername(request.getUsername());

        if (userFromDb.getPassword().equals(request.getOldPassword())) {
            if (request.getOldPassword().equals(request.getNewPassword())) {
                jsonResponse.put("error", "New password cannot be the same as the old password.");
                return ResponseEntity.status(400).body(gson.toJson(jsonResponse));

            }
            userFromDb.setPassword(request.getNewPassword());
            userRepository.save(userFromDb);

            LogoutRequest logoutRequest = new LogoutRequest();
            logoutRequest.setToken(request.getToken());
            logoutRequest.setUsername(request.getUsername());
            authController.logout(logoutRequest);

        } else {
            jsonResponse.put("error", "Invalid old password. Please provide a valid old password.");
            return ResponseEntity.status(400).body(gson.toJson(jsonResponse));
        }

        jsonResponse.put("message", "Password changed successfully.");
        return ResponseEntity.status(200).body(gson.toJson(jsonResponse));

    }

    public ResponseEntity<String> getAllUsersAndStudents() {
        Gson gson = new Gson();
        Map<String, String> jsonResponse = new HashMap<>();

        jsonResponse.put("users", gson.toJson(userRepository.findAll().size()));
        jsonResponse.put("students", gson.toJson(studentRepository.findAll().size()));

        return ResponseEntity.status(200).body(gson.toJson(jsonResponse));
    }
}
