package com.kasv.gunda.bootcamp.controllers;

import com.kasv.gunda.bootcamp.services.StudentService;
import com.kasv.gunda.bootcamp.services.TokenService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/all")
    public String getAllStudents(@RequestHeader("Authorization") String token) {
        token = token.replace("Bearer ", "");
        return studentService.getAllStudents(token);
    }
}
