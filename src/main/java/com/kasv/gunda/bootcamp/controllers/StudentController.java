package com.kasv.gunda.bootcamp.controllers;

import com.google.gson.Gson;
import com.kasv.gunda.bootcamp.models.Student;
import com.kasv.gunda.bootcamp.models.StudentRegistration;
import com.kasv.gunda.bootcamp.payload.request.StudentUpdateRequest;
import com.kasv.gunda.bootcamp.payload.response.MessageResponse;
import com.kasv.gunda.bootcamp.services.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.kasv.gunda.bootcamp.security.jwt.JwtUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    JwtUtils jwtUtils;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /* Returns all students,
     while admin can view all students,
     students can only view their first name
     and first letter of their last name */
    @GetMapping("/students")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> getAllStudents() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        return ResponseEntity.ok(studentService.getAllStudents(authorities));
    }

    @GetMapping("/student/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> getStudentById(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return studentService.getStudentById(id);
        } else {
            return ResponseEntity.status(403).body("You are not authorized to view this resource.");
        }
    }

    /* Registers a new student */
    @PostMapping("/student/register")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> registerStudent(@RequestBody StudentRegistration studentForm, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String authorizationHeader = request.getHeader("Authorization");

        return studentService.registerStudent(studentForm, authorities, authorizationHeader);
    }

    /* Updates student data */
    @PutMapping("/student/update/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> updateStudent(@PathVariable Long id, @RequestBody StudentUpdateRequest updateRequest, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String authorizationHeader = request.getHeader("Authorization");

        return studentService.updateStudent(id, updateRequest, authorities, authorizationHeader);
    }

    /* Returns the count of all students */
    @GetMapping("/students/count")
    public ResponseEntity<String> getStudentsCount() {

        Gson gson = new Gson();
        Map<String, Integer> jsonResponse = new HashMap<>();
        jsonResponse.put("count", studentService.getStudentsCount());
        return ResponseEntity.ok(gson.toJson(jsonResponse));
    }
}
