package com.kasv.gunda.bootcamp.services;

import com.kasv.gunda.bootcamp.controllers.AuthController;
import com.kasv.gunda.bootcamp.entities.LogoutRequest;
import com.kasv.gunda.bootcamp.entities.User;
import com.kasv.gunda.bootcamp.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthController authController;

    private TokenService tokenService = TokenService.getInstance();

    public UserService(UserRepository userRepository, AuthController authController) {
        this.userRepository = userRepository;
        this.authController = authController;
    }


    public ResponseEntity<String> changePassword(String token, String newPassword, String oldPassword, String username) {
        if (!tokenService.isTokenValid(token)) {
            return ResponseEntity.status(401).body("Invalid token. Please provide a valid token.");
        }

        User userFromDb = userRepository.findByUsername(username);

        if (userFromDb.getPassword().equals(oldPassword)) {
            if (oldPassword.equals(newPassword)) {
                return ResponseEntity.status(400).body("New password cannot be the same as the old password.");
            }
            userFromDb.setPassword(newPassword);
            userRepository.save(userFromDb);

            //logout
            LogoutRequest logoutRequest = new LogoutRequest();
            logoutRequest.setToken(token);
            logoutRequest.setUsername(username);
            authController.logout(logoutRequest);

        } else {
            return ResponseEntity.status(401).body("Invalid old password. Please provide a valid old password.");
        }

        return ResponseEntity.ok("Password changed successfully.");

    }
}
