package com.kasv.gunda.bootcamp.services;

import com.google.gson.Gson;
import com.kasv.gunda.bootcamp.entities.LoginRequest;
import com.kasv.gunda.bootcamp.entities.LogoutRequest;
import com.kasv.gunda.bootcamp.entities.User;
import com.kasv.gunda.bootcamp.utilities.*;
import com.kasv.gunda.bootcamp.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final EmailFunctions emailFunctions;
    private TokenFunctions tokenFunctions = TokenFunctions.getInstance();
    private UserTimeoutFunctions userTimeoutFunctions = UserTimeoutFunctions.getInstance();

    public AuthService(UserRepository userRepository, EmailFunctions emailFunctions) {
        this.userRepository = userRepository;
        this.emailFunctions = emailFunctions;
    }

    public ResponseEntity<String> login(LoginRequest loginRequest) {

        Gson gson = new Gson();
        Map<String, String> jsonResponse = new HashMap<>();

        User userFromDb = userRepository.findByUsername(loginRequest.getUsername());

        if (userFromDb != null) {
            if (userFromDb.getPassword().equals(loginRequest.getPassword()) && userTimeoutFunctions.isUserOnTimeout(userFromDb.getId()) == false){

                String token = TokenGenerator.generateToken();
                tokenFunctions.storeToken((long) userFromDb.getId(), token);

                jsonResponse.put("token", token);
                jsonResponse.put("username", userFromDb.getUsername());

                userTimeoutFunctions.resetBadPasswordsCount(userFromDb.getId());
                return ResponseEntity.status(200).body(gson.toJson(jsonResponse));

            } else {
                if (userTimeoutFunctions.isUserOnTimeout(userFromDb.getId())) {
                    jsonResponse.put("message", "Your account is locked. Please try again after 5 seconds.");
                    return ResponseEntity.status(401).body(gson.toJson(jsonResponse));
                }
                if (userTimeoutFunctions.getBadPasswordsCount(userFromDb.getId()) < 3) {
                    userTimeoutFunctions.incrementCount((long) userFromDb.getId());
                }
                if (userTimeoutFunctions.getBadPasswordsCount(userFromDb.getId()) >= 3 ) {
                    userTimeoutFunctions.setUserTimeout( userFromDb.getId());
                    jsonResponse.put("message", "Your account has been locked. Please try again after 5 seconds.");
                    return ResponseEntity.status(401).body(gson.toJson(jsonResponse));
                }
                jsonResponse.put(
                        "error",
                        "Invalid credentials. Please provide valid credentials. " +
                        userTimeoutFunctions.getBadPasswordsCount(userFromDb.getId()) +
                        "/3 failed attempts."
                );
                return ResponseEntity.status(401).body(gson.toJson(jsonResponse));
            }
        } else {
            jsonResponse.put("error", "User with username " + loginRequest.getUsername() + " does not exist. Please provide valid credentials.");
            return ResponseEntity.status(404).body(gson.toJson(jsonResponse));
        }
    }

    public ResponseEntity<String> logout(LogoutRequest logoutRequest) {

        Gson gson = new Gson();
        Map<String, String> jsonResponse = new HashMap<>();

        long userId = (long) userRepository.findIdByUsername(logoutRequest.getUsername());

        if (tokenFunctions.isTokenExists(userId)) {
            if (tokenFunctions.isTokenValid(logoutRequest.getToken())) {
                tokenFunctions.removeToken(userId);
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

    public ResponseEntity<String> forgotPassword(User user) {

        Gson gson = new Gson();
        Map<String, String> jsonResponse = new HashMap<>();

        User userFromDb = userRepository.findByUsername(user.getUsername());

        if (userFromDb != null) {
            if (userFromDb.getEmail().equals(user.getEmail())) {
                String newPassword = RandomPasswordGenerator.generateCode();
                userFromDb.setPassword(newPassword);
                userRepository.save(userFromDb);
                emailFunctions.sendNewPassword(userFromDb.getEmail(), newPassword);
                jsonResponse.put("message", "New password has been sent to your email.");
                return ResponseEntity.status(200).body(gson.toJson(jsonResponse));
            } else {
                jsonResponse.put("error", "Invalid email. Please provide valid email.");
                return ResponseEntity.status(400).body(gson.toJson(jsonResponse));
            }
        } else {
            jsonResponse.put("error", "User with username " + user.getUsername() + " does not exist.");
            return ResponseEntity.status(404).body(gson.toJson(jsonResponse));
        }
    }
}
