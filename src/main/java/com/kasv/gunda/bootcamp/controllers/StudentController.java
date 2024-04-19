package com.kasv.gunda.bootcamp.controllers;

import com.google.gson.Gson;
import com.kasv.gunda.bootcamp.entities.LoginRequest;
import com.kasv.gunda.bootcamp.entities.LogoutRequest;
import com.kasv.gunda.bootcamp.entities.Student;
import com.kasv.gunda.bootcamp.services.StudentService;
import com.kasv.gunda.bootcamp.services.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
//@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/users")
    public String getAllStudents(@RequestBody(required = false) LoginRequest loginRequest) {

        return studentService.getAllStudents(loginRequest);

    }

    @PostMapping("/user/{id}")
    public ResponseEntity<String> getStudentById(@PathVariable Long id, @RequestBody LoginRequest loginRequest) {
        Gson gson = new Gson();
        Map<String, String> jsonResponse = new HashMap<>();

        if (loginRequest.getUsername() == null ||
                loginRequest.getToken() == null ||
                loginRequest.getUsername().isEmpty() ||
                loginRequest.getToken().isEmpty() ||
                id == null) {

            jsonResponse.put("error", "Invalid request. Please provide valid input data.");

            return ResponseEntity.status(400).body(gson.toJson(jsonResponse));
        }

        return studentService.getStudentById(id, loginRequest);
    }

    @PostMapping("/user/register")
    public ResponseEntity<String> registerStudent(@RequestBody Student student) {

        return studentService.registerStudent(student);
    }

    @PutMapping("/student/update/{id}")
    public ResponseEntity<String> updateLastName(@PathVariable Long id, @RequestBody LogoutRequest details) {

        if(details.getLastName() == null || details.getLastName().isEmpty() || id == null
                || details.getToken() == null || details.getToken().isEmpty()) {
            return ResponseEntity.status(400).body("Invalid request. Please provide valid input data.");
        }

        return studentService.updateLastName(id, details);
    }


}
