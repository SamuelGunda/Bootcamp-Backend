package com.kasv.gunda.bootcamp.services;

import com.google.gson.Gson;
import com.kasv.gunda.bootcamp.models.Student;
import com.kasv.gunda.bootcamp.models.StudentRegistration;
import com.kasv.gunda.bootcamp.payload.request.StudentUpdateRequest;
import com.kasv.gunda.bootcamp.repositories.StudentRegistrationRepository;
import com.kasv.gunda.bootcamp.repositories.StudentRepository;
import com.kasv.gunda.bootcamp.repositories.UserRepository;
import com.kasv.gunda.bootcamp.security.jwt.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StudentService {

    private final JwtUtils jwtUtils;
    private final StudentRepository studentRepository;
    private final StudentRegistrationRepository studentRegistrationRepository;
    private final UserRepository userRepository;

    public StudentService(JwtUtils jwtUtils, StudentRepository studentRepository, StudentRegistrationRepository studentRegistrationRepository, UserRepository userRepository) {
        this.jwtUtils = jwtUtils;
        this.studentRepository = studentRepository;
        this.studentRegistrationRepository = studentRegistrationRepository;
        this.userRepository = userRepository;
    }

    public String getAllStudents(Collection<? extends GrantedAuthority> authorities) {

        Gson gson = new Gson();
        List<Student> students = studentRepository.findAll();

        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {

            return gson.toJson(students);

        }

        for (Student student : students) {
            student.setDob(null);
            student.setLastName(student.getLastName().substring(0, 1));
        }

        return gson.toJson(students);
    }

    public int getStudentsCount() {
        return (int) studentRepository.count();
    }

    public ResponseEntity<String> registerStudent(StudentRegistration sur, Collection<? extends GrantedAuthority> authorities , String authorizationHeader) {
        Gson gson = new Gson();
        Map<String, String> jsonResponse = new HashMap<>();

        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            try {
                Student student = new Student();
                student.setFirstName(sur.getFirstName());
                student.setLastName(sur.getLastName());
                student.setDob(sur.getDob());
                student.setUser(userRepository.getUserById(sur.getUserId()));
                studentRepository.save(student);
                return ResponseEntity.status(201).body("");
            } catch (Exception e) {
                jsonResponse.put("error", "Somethings wrong I can feel it");
                return ResponseEntity.status(403).body(gson.toJson(jsonResponse));
            }
        }

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            long usersId = jwtUtils.getUserIdFromJwtToken(token);

            if (usersId != sur.getUserId()) {
                jsonResponse.put("error", "You are not authorized to perform this action.");
                return ResponseEntity.status(403).body(gson.toJson(jsonResponse));
            }

            try {
                studentRegistrationRepository.save(sur);
                return ResponseEntity.status(201).body("Your registration is pending approval.");
            } catch (Exception e) {
                jsonResponse.put("error", e.getMessage());
                return ResponseEntity.status(403).body(gson.toJson(jsonResponse));
            }
        }

        jsonResponse.put("error", "You are not authorized to perform this action.");
        return ResponseEntity.status(403).body(gson.toJson(jsonResponse));
    }

    public ResponseEntity<String> getStudentById(Long id) {

            Gson gson = new Gson();
            Map<String, String> jsonResponse = new HashMap<>();

            if (!studentRepository.existsById(id)) {
                jsonResponse.put("error", "Student with id " + id + " not found.");
                return ResponseEntity.status(404).body(gson.toJson(jsonResponse));
            }

            return ResponseEntity.status(200).body(gson.toJson(studentRepository.findById(id)));
    }

    public ResponseEntity<String> updateStudent(long id, StudentUpdateRequest sur, Collection<? extends GrantedAuthority> authorities, String  authorizationHeader) {

        Gson gson = new Gson();
        Map<String, String> jsonResponse = new HashMap<>();

        if (sur == null || !studentRepository.existsById(id)) {
            jsonResponse.put("error", "Invalid request. Please provide valid input data.");
            return ResponseEntity.status(400).body(gson.toJson(jsonResponse));
        }

        Student studentFromDatabase = studentRepository.findById(id);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            long usersId = jwtUtils.getUserIdFromJwtToken(token);

            if (usersId != studentFromDatabase.getUser().getId()) {
                if (authorities.stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){
                    jsonResponse.put("error", "You are not authorized to perform this action.");
                    return ResponseEntity.status(403).body(gson.toJson(jsonResponse));
                }
            }
        }

        studentFromDatabase.setFirstName(sur.getFirstName() != null ? sur.getFirstName() : studentFromDatabase.getFirstName());
        studentFromDatabase.setLastName(sur.getLastName() != null ? sur.getLastName() : studentFromDatabase.getLastName());
        studentFromDatabase.setDob(sur.getDob() != null ? sur.getDob() : studentFromDatabase.getDob());

        studentRepository.save(studentFromDatabase);
        return ResponseEntity.status(200).body("");
    }
}
