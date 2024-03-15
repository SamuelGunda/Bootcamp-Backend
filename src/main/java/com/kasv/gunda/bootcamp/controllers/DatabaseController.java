package com.kasv.gunda.bootcamp.controllers;


import com.kasv.gunda.bootcamp.services.DatabaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/database")
public class DatabaseController {

    private final DatabaseService dbService;

    public DatabaseController(DatabaseService dbService) {
        this.dbService = dbService;
    }

    @GetMapping("/connection")
    public ResponseEntity<String> connection() {
        if (dbService.connectToDb().equals("Connected to database")) {
            return ResponseEntity.ok("Connected to database");
        } else {
            return ResponseEntity.status(500).body("Failed to connect to database");
        }
    }
}
