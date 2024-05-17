package com.kasv.gunda.bootcamp.controllers;

import com.google.gson.Gson;
import com.kasv.gunda.bootcamp.exceptions.ApplicationAlreadyProcessedException;
import com.kasv.gunda.bootcamp.exceptions.InvalidActionException;
import com.kasv.gunda.bootcamp.exceptions.ResourceNotFoundException;
import com.kasv.gunda.bootcamp.exceptions.UnauthorizedAccessException;
import com.kasv.gunda.bootcamp.models.Status;
import com.kasv.gunda.bootcamp.models.StudentApplication;
import com.kasv.gunda.bootcamp.services.StudentApplicationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class StudentApplicationController {

    private final StudentApplicationService studentApplicationService;

    public StudentApplicationController(StudentApplicationService studentApplicationService) {
        this.studentApplicationService = studentApplicationService;
    }

    /*
    * Returns all student applications
    * Only admin can view all applications
    * */

    @GetMapping("/applications")
    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> getAllApplications() {
        Gson gson = new Gson();
        try {
            List<StudentApplication> studentApplications = studentApplicationService.getAllApplications();
            if (studentApplications.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(gson.toJson(studentApplications));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /*
    * Returns student application by id
    * Only admin can view application by id
    * */

    @GetMapping("/applications/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> getApplicationById(@PathVariable Long id, HttpServletRequest request) {
        Gson gson = new Gson();
        Map<String, String> jsonResponse = new HashMap<>();
        try {
            StudentApplication studentApplication = studentApplicationService.getApplicationById(id);
            if (studentApplication == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(gson.toJson(studentApplication));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/applications/{id}/{action}")
    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateApplication(@PathVariable Long id, @PathVariable String action) {
        Gson gson = new Gson();
        Map<String, String> jsonResponse = new HashMap<>();
        try {
            StudentApplication application = studentApplicationService.updateApplication(id, action);
            String responseMessage;
            if (application.getStatus() == Status.ACCEPTED) {
                responseMessage = "Application accepted";
            } else {
                responseMessage = "Application declined";
            }
            jsonResponse.put("message", responseMessage);
            return ResponseEntity.ok(gson.toJson(jsonResponse));
        } catch (ApplicationAlreadyProcessedException | InvalidActionException e) {
            jsonResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(gson.toJson(jsonResponse));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/applications")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> createApplication(@RequestBody StudentApplication application, HttpServletRequest request) {
        Gson gson = new Gson();
        Map<String, String> jsonResponse = new HashMap<>();

        try {
            studentApplicationService.createApplication(application, request.getHeader("Authorization"));
            jsonResponse.put("message", "Application submitted successfully.");
            return ResponseEntity.ok(gson.toJson(jsonResponse));
        } catch (IllegalArgumentException e) {
            jsonResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(gson.toJson(jsonResponse));
        } catch (UnauthorizedAccessException e) {
            jsonResponse.put("error", e.getMessage());
            return ResponseEntity.status(403).body(gson.toJson(jsonResponse));
        } catch (RuntimeException e) {
            jsonResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(gson.toJson(jsonResponse));
        }
    }

}
