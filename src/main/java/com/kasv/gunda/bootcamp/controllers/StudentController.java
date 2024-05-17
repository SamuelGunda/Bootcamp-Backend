package com.kasv.gunda.bootcamp.controllers;

import com.google.gson.Gson;
import com.kasv.gunda.bootcamp.payload.response.StudentResponse;
import com.kasv.gunda.bootcamp.exceptions.InvalidActionException;
import com.kasv.gunda.bootcamp.models.StudentApplication;
import com.kasv.gunda.bootcamp.payload.request.StudentUpdateRequest;
import com.kasv.gunda.bootcamp.services.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.kasv.gunda.bootcamp.security.jwt.JwtUtils;

import java.util.Collection;

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

    /*
     * Returns all student data
     * Admin can view full student data
     * Basic users can only see student first name and first letter of last name
     * */

    @GetMapping("/students")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> getAllStudents() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        return ResponseEntity.ok(studentService.getAllStudents(authorities));
    }

    /*
     * Returns student data by id
     * Admin can view all student data
     * Basic users can only view their own data
     * */

    @GetMapping("/students/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> getStudentById(@PathVariable Long id, HttpServletRequest request) {
        Gson gson = new Gson();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String authorizationHeader = request.getHeader("Authorization");

        try {
            StudentResponse student = studentService.getStudentById(id, authorities, authorizationHeader);
            return ResponseEntity.ok(gson.toJson(student));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (InvalidActionException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    /*
     * Updates student data by id
     * Admin can update all student data
     * Basic users can only update their own data
     * */

    @PutMapping("/students/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> updateStudent(@PathVariable Long id, @RequestBody StudentUpdateRequest updateRequest, HttpServletRequest request) {
        Gson gson = new Gson();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String authorizationHeader = request.getHeader("Authorization");

        try {
            StudentResponse student = studentService.updateStudent(id, updateRequest, authorities, authorizationHeader);
            return ResponseEntity.ok(gson.toJson(student));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch ( InvalidActionException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    /*
    * Registers a new student
    * Admin can register a new student
    * */

    @PostMapping("/students")
    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> registerStudent(@RequestBody StudentApplication studentForm) {
        try {
            return ResponseEntity.ok(studentService.registerStudent(studentForm));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    /* Returns the count of all students */

    @GetMapping("/count")
    public ResponseEntity<Integer> getStudentsCount() {
        return ResponseEntity.ok(studentService.getStudentsCount());
    }
}
