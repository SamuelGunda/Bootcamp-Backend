package com.kasv.gunda.bootcamp.controllers;

import com.google.gson.Gson;
import com.kasv.gunda.bootcamp.entities.AuthCheck;
import com.kasv.gunda.bootcamp.entities.LogoutRequest;
import com.kasv.gunda.bootcamp.entities.Student;
import com.kasv.gunda.bootcamp.services.StudentService;
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

    @PostMapping("/students")
    public String getAllStudents(@RequestBody(required = false) AuthCheck authCheck) {

        return studentService.getAllStudents(authCheck);

    }

    @PostMapping("/student/{id}")
    public ResponseEntity<String> getStudentById(@PathVariable Long id, @RequestBody AuthCheck authCheck) {

        Gson gson = new Gson();
        Map<String, String> jsonResponse = new HashMap<>();

        if (authCheck.getUsername() == null ||
                authCheck.getToken() == null ||
                authCheck.getUsername().isEmpty() ||
                authCheck.getToken().isEmpty() ||
                id == null) {

            jsonResponse.put("error", "Invalid request. Please provide valid input data.");

            return ResponseEntity.status(400).body(gson.toJson(jsonResponse));
        }

        return studentService.getStudentById(id, authCheck);
    }

    @PostMapping("/student/register")
    public ResponseEntity<String> registerStudent(@RequestBody Student student) {

        Gson gson = new Gson();
        Map<String, String> jsonResponse = new HashMap<>();

        if (student.getFirstName() == null || student.getFirstName().isEmpty() ||
                student.getLastName() == null || student.getLastName().isEmpty() ||
                student.getDob() == null) {
            jsonResponse.put("error", "Invalid request. Please provide valid input data.");
            return ResponseEntity.status(400).body(gson.toJson(jsonResponse));
        }

        return studentService.registerStudent(student);
    }

    @PutMapping("/student/update/{id}")
    public ResponseEntity<String> updateLastName(@PathVariable Long id, @RequestBody LogoutRequest details) {

        Gson gson = new Gson();
        Map<String, String> jsonResponse = new HashMap<>();

        if(details.getLastName() == null || details.getLastName().isEmpty() || id == null
                || details.getToken() == null || details.getToken().isEmpty()) {
            jsonResponse.put("error", "Invalid request. Please provide valid input data.");
            return ResponseEntity.status(400).body(gson.toJson(jsonResponse));
        }

        return studentService.updateLastName(id, details);
    }


}
