package com.kasv.gunda.bootcamp.controllers;

import com.kasv.gunda.bootcamp.models.StudentApplication;
import com.kasv.gunda.bootcamp.payload.request.StudentUpdateRequest;
import com.kasv.gunda.bootcamp.services.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> getStudentById(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return studentService.getStudentById(id);
        } else {
            return ResponseEntity.status(403).body("You are not authorized to view this resource.");
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String authorizationHeader = request.getHeader("Authorization");

        return studentService.updateStudent(id, updateRequest, authorities, authorizationHeader);
    }

    /*
    * Registers a new student
    * Admin can register a new student
    * Basic users can only register themselves and must be approved by an admin
    * */

    @PostMapping("/students")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> registerStudent(@RequestBody StudentApplication studentForm, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String authorizationHeader = request.getHeader("Authorization");

        return studentService.registerStudent(studentForm, authorities, authorizationHeader);
    }

    /* Returns the count of all students */

    @GetMapping("/count")
    public ResponseEntity<Integer> getStudentsCount() {
        return ResponseEntity.ok(studentService.getStudentsCount());
    }
}
