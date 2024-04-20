package com.kasv.gunda.bootcamp.controllers;


import com.google.gson.Gson;
import com.kasv.gunda.bootcamp.services.DatabaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/database")
public class DatabaseController {

    private final DatabaseService dbService;

    public DatabaseController(DatabaseService dbService) {
        this.dbService = dbService;
    }

    @GetMapping("/connection")
    public ResponseEntity<String> connection() {
        Gson gson = new Gson();
        Map<String, String> jsonResponse = new HashMap<>();

        if (dbService.connectToDb().equals("Connected to database")) {
            jsonResponse.put("message", "Connected to database");
            return ResponseEntity.status(200).body(gson.toJson(jsonResponse));

        } else {
            jsonResponse.put("message", "Failed to connect to database");
            return ResponseEntity.status(500).body(gson.toJson(jsonResponse));
        }
    }
}
