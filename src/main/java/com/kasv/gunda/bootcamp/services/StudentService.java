package com.kasv.gunda.bootcamp.services;

import com.google.gson.Gson;
import com.kasv.gunda.bootcamp.entities.Student;
import com.kasv.gunda.bootcamp.repositories.StudentRepository;
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

        if (!tokenService.isTokenValid(token)) {

            for (Student student : students) {
                student.setDob(null);
                student.setLastName(student.getLastName().substring(0, 1));
            }

        }
        return gson.toJson(students);
    }
}
