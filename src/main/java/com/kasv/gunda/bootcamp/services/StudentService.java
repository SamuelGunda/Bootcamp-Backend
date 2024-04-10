package com.kasv.gunda.bootcamp.services;

import com.google.gson.Gson;
import com.kasv.gunda.bootcamp.entities.LogoutRequest;
import com.kasv.gunda.bootcamp.entities.Student;
import com.kasv.gunda.bootcamp.repositories.StudentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentService {

    private TokenService tokenService = TokenService.getInstance();
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public String getAllStudents(String token) {
        Gson gson = new Gson();
        Map<String, String> jsonResponse = new HashMap<>();

        List<Student> students = studentRepository.findAll();

        if (token != null && !token.isEmpty()) {
            if (!tokenService.isTokenValid(token)) {

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

    public ResponseEntity<String> getStudentById(Long id, String token) {
        Gson gson = new Gson();
        Map<String, String> jsonResponse = new HashMap<>();

        if (!tokenService.isTokenValid(token)) {
            return ResponseEntity.status(401).body("Invalid token. Please provide a valid token.");
        }

        if (!studentRepository.existsById(id)) {
            return ResponseEntity.status(404).body("Student with id " + id + " not found.");
        }

        return ResponseEntity.status(200).body(gson.toJson(studentRepository.findById(id)));
    }

    public ResponseEntity<String> registerStudent(Student student) {

        if (student == null) {
            return ResponseEntity.status(400).body("Invalid request. Please provide valid input data.");
        }

        if (student.getFirstName() == null || student.getFirstName().isEmpty() ||
                student.getLastName() == null || student.getLastName().isEmpty() ||
                student.getDob() == null) {
            return ResponseEntity.status(400).body("Invalid request. Please provide valid input data.");
        }

        if(studentRepository.existsById(student.getId())) {
            return ResponseEntity.status(400).body("Student with id " + student.getId() + " already exists.");
        }

        studentRepository.save(student);

        return ResponseEntity.status(201).body("{}");
    }


    public ResponseEntity<String> updateLastName(Long id, LogoutRequest details) {

        if (!tokenService.isTokenValid(details.getToken())) {
            return ResponseEntity.status(401).body("Invalid token. Please provide a valid token.");
        }

        if (!studentRepository.existsById(id)) {
            return ResponseEntity.status(404).body("Student with id " + id + " not found.");
        }

        Student student = studentRepository.findById(id);
        student.setLastName(details.getLastName());

        studentRepository.save(student);

        return ResponseEntity.status(200).body("{}");
    }
}
