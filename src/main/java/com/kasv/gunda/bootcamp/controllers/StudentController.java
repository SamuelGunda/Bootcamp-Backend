package com.kasv.gunda.bootcamp.controllers;

import com.google.gson.Gson;
import com.kasv.gunda.bootcamp.models.Student;
import com.kasv.gunda.bootcamp.payload.response.MessageResponse;
import com.kasv.gunda.bootcamp.services.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.kasv.gunda.bootcamp.security.jwt.JwtUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
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
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public String getAllStudents(HttpServletRequest request) {
//        studentService.getAllStudents(request);
        System.out.println(request.getHeader("Authorization"));
        System.out.println(Arrays.toString(request.getCookies()));
        return "All students";
    }

    @PostMapping("/student/{id}")
    public ResponseEntity<String> getStudentById(@PathVariable Long id) {
        return null;
    }

    /* Registers a new student */
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

    /* Updates student data */
    @PutMapping("/student/update/{id}")
    public ResponseEntity<String> updateStudent( @PathVariable Long id, @RequestBody Student student) {
        return null;
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
