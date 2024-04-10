package com.kasv.gunda.bootcamp.controllers;

import com.kasv.gunda.bootcamp.entities.LogoutRequest;
import com.kasv.gunda.bootcamp.entities.Student;
import com.kasv.gunda.bootcamp.services.StudentService;
import com.kasv.gunda.bootcamp.services.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/users")
    public String getAllStudents(@RequestBody(required = false) String token) {

        return studentService.getAllStudents(token);

    }

    @GetMapping("/user/{id}")
    public ResponseEntity<String> getStudentById(@PathVariable Long id, @RequestBody String token) {

        if(token == null || token.isEmpty() || id == null) {
            return ResponseEntity.status(400).body("Invalid request. Please provide valid input data.");
        }

        return studentService.getStudentById(id, token);
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
