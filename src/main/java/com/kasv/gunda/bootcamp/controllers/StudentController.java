package com.kasv.gunda.bootcamp.controllers;

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
    public String getAllStudents(@RequestBody String token) {
//        token = token.replace("Bearer ", "");
        return studentService.getAllStudents(token);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<String> getStudentById(@PathVariable Long id, @RequestBody String token) {
//        token = token.replace("Bearer ", "");
        return studentService.getStudentById(id, token);
    }

}
