package com.kasv.gunda.bootcamp.services;

import com.google.gson.Gson;
import com.kasv.gunda.bootcamp.entities.AuthCheck;
import com.kasv.gunda.bootcamp.entities.LogoutRequest;
import com.kasv.gunda.bootcamp.entities.Student;
import com.kasv.gunda.bootcamp.repositories.StudentRepository;
import com.kasv.gunda.bootcamp.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentService {

    private TokenService tokenService = TokenService.getInstance();
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public StudentService(StudentRepository studentRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    public String getAllStudents(AuthCheck authCheck) {

        Gson gson = new Gson();
        Map<String, String> jsonResponse = new HashMap<>();

        List<Student> students = studentRepository.findAll();

        if (authCheck.getToken() != null && !authCheck.getToken().isEmpty()) {
            if (!tokenService.isTokenValid(authCheck.getToken())) {

                for (Student student : students) {
                    student.setDob(null);
                    student.setLastName(student.getLastName().substring(0, 1));
                }

            }
            return gson.toJson(students);
        } else {
            for (Student student : students) {
                student.setDob(null);
                student.setLastName(student.getLastName().substring(0, 1));
            }
            return gson.toJson(students);
        }
    }

    public ResponseEntity<String> getStudentById(Long id, AuthCheck authCheck) {

        Gson gson = new Gson();
        Map<String, String> jsonResponse = new HashMap<>();

        long userId = (long) userRepository.findIdByUsername(authCheck.getUsername());

        if (tokenService.isTokenExists(userId)) {
            if (!tokenService.isTokenValid(authCheck.getToken())) {
                jsonResponse.put("error", "Invalid token. Please provide valid token.");
                return ResponseEntity.status(401).body(gson.toJson(jsonResponse));
            }
        } else {
            jsonResponse.put("error", "Invalid token. Please provide valid token.");
            return ResponseEntity.status(401).body(gson.toJson(jsonResponse));
        }

        if (!studentRepository.existsById(id)) {
            jsonResponse.put("error", "Student with id " + id + " not found.");
            return ResponseEntity.status(404).body(gson.toJson(jsonResponse));
        }

        return ResponseEntity.status(200).body(gson.toJson(studentRepository.findById(id)));
    }

    public ResponseEntity<String> registerStudent(Student student) {

        Gson gson = new Gson();
        Map<String, String> jsonResponse = new HashMap<>();

        if (student == null) {
            jsonResponse.put("error", "Invalid request. Please provide valid input data.");
            return ResponseEntity.status(400).body(gson.toJson(jsonResponse));
        }

        if (student.getFirstName() == null || student.getFirstName().isEmpty() ||
                student.getLastName() == null || student.getLastName().isEmpty() ||
                student.getDob() == null) {
            jsonResponse.put("error", "Invalid request. Please provide valid input data.");
            return ResponseEntity.status(400).body(gson.toJson(jsonResponse));
        }

        if(studentRepository.existsById(student.getId())) {
            jsonResponse.put("error", "Student with id " + student.getId() + " already exists.");
            return ResponseEntity.status(400).body(gson.toJson(jsonResponse));
        }

        studentRepository.save(student);

        return ResponseEntity.status(201).body("{}");
    }


    public ResponseEntity<String> updateLastName(Long id, LogoutRequest details) {

        Gson gson = new Gson();
        Map<String, String> jsonResponse = new HashMap<>();

        if (!tokenService.isTokenValid(details.getToken())) {
            jsonResponse.put("error", "Invalid request. Please provide valid input data.");
            return ResponseEntity.status(400).body(gson.toJson(jsonResponse));
        }

        if (!studentRepository.existsById(id)) {
            jsonResponse.put("error", "Student with id " + id + " not found.");
            return ResponseEntity.status(404).body(gson.toJson(jsonResponse));
        }

        Student student = studentRepository.findById(id);
        student.setLastName(details.getLastName());

        studentRepository.save(student);

        return ResponseEntity.status(200).body("{}");
    }
}
