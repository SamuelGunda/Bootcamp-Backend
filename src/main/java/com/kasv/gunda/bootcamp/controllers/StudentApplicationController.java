package com.kasv.gunda.bootcamp.controllers;

import com.kasv.gunda.bootcamp.services.StudentApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class StudentApplicationController {

    private final StudentApplicationService studentApplicationService;

    public StudentApplicationController(StudentApplicationService studentApplicationService) {
        this.studentApplicationService = studentApplicationService;
    }

    @GetMapping("/applications")
    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> getAllApplications() {
        return ResponseEntity.ok(studentApplicationService.getAllApplications());
    }

    @GetMapping("/applications/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> getApplicationById(@PathVariable Long id) {
        return studentApplicationService.getApplicationById(id);
    }

}
