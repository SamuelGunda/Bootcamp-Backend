package com.kasv.gunda.bootcamp.services;

import com.google.gson.Gson;
import com.kasv.gunda.bootcamp.payload.response.StudentResponse;
import com.kasv.gunda.bootcamp.exceptions.InvalidActionException;
import com.kasv.gunda.bootcamp.models.Student;
import com.kasv.gunda.bootcamp.models.StudentApplication;
import com.kasv.gunda.bootcamp.payload.request.StudentUpdateRequest;
import com.kasv.gunda.bootcamp.repositories.StudentApplicationRepository;
import com.kasv.gunda.bootcamp.repositories.StudentRepository;
import com.kasv.gunda.bootcamp.repositories.UserRepository;
import com.kasv.gunda.bootcamp.security.jwt.JwtUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StudentService {

    private final JwtUtils jwtUtils;
    private final StudentRepository studentRepository;
    private final StudentApplicationRepository studentApplicationRepository;
    private final UserRepository userRepository;

    public StudentService(JwtUtils jwtUtils, StudentRepository studentRepository, StudentApplicationRepository studentApplicationRepository, UserRepository userRepository) {
        this.jwtUtils = jwtUtils;
        this.studentRepository = studentRepository;
        this.studentApplicationRepository = studentApplicationRepository;
        this.userRepository = userRepository;
    }

    public String getAllStudents(Collection<? extends GrantedAuthority> authorities) {

        Gson gson = new Gson();
        List<Student> studentsFromDb = studentRepository.findAll();
        List<StudentResponse> studentList = new ArrayList<>();

        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            for (Student student : studentsFromDb) {
                studentList.add(StudentResponse.builder()
                        .id(student.getId())
                        .firstName(student.getFirstName())
                        .lastName(student.getLastName())
                        .dob(student.getDob())
                        .build());
            }
            return gson.toJson(studentList);

        }

        for (Student student : studentsFromDb) {
            studentList.add(StudentResponse.builder()
                    .id(student.getId())
                    .firstName(student.getFirstName())
                    .lastName(student.getLastName().substring(0, 1))
                    .build());
        }

        return gson.toJson(studentList);
    }

    public int getStudentsCount() {
        return (int) studentRepository.count();
    }

    public String registerStudent(StudentApplication sur) {

        if (sur == null) {
            throw new IllegalArgumentException("Invalid request.");
        }

        try {
            Student student = new Student();
            student.setFirstName(sur.getFirstName());
            student.setLastName(sur.getLastName());
            student.setDob(sur.getDob());
            student.setUser(userRepository.getUserById(sur.getUserId()));
            studentRepository.save(student);
            return "Student registered successfully!";
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public StudentResponse getStudentById(Long id, Collection<? extends GrantedAuthority> authorities, String authorizationHeader) {
        if (!studentRepository.existsById(id)) {
            throw new IllegalArgumentException("Invalid request.");
        }

        Student studentFromDatabase = studentRepository.findById(id);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            long usersId = jwtUtils.getUserIdFromJwtToken(token);

            if (usersId != studentFromDatabase.getUser().getId()) {
                if (authorities.stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){
                    throw new InvalidActionException("You are not authorized to view this resource.");
                }
            }
        }

        return StudentResponse.builder()
                .id(studentFromDatabase.getId())
                .firstName(studentFromDatabase.getFirstName())
                .lastName(studentFromDatabase.getLastName())
                .dob(studentFromDatabase.getDob())
                .build();
    }

    public StudentResponse updateStudent(long id, StudentUpdateRequest sur, Collection<? extends GrantedAuthority> authorities, String  authorizationHeader) {

        if (sur == null || !studentRepository.existsById(id)) {
            throw new IllegalArgumentException("Invalid request.");
        }

        Student studentFromDatabase = studentRepository.findById(id);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            long usersId = jwtUtils.getUserIdFromJwtToken(token);

            if (usersId != studentFromDatabase.getUser().getId()) {
                if (authorities.stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){
                    throw new InvalidActionException("You are not authorized to update this resource.");
                }
            }
        }

        studentFromDatabase.setFirstName(sur.getFirstName() != null ? sur.getFirstName() : studentFromDatabase.getFirstName());
        studentFromDatabase.setLastName(sur.getLastName() != null ? sur.getLastName() : studentFromDatabase.getLastName());
        studentFromDatabase.setDob(sur.getDob() != null ? sur.getDob() : studentFromDatabase.getDob());

        studentRepository.save(studentFromDatabase);

        return StudentResponse.builder()
                .id(studentFromDatabase.getId())
                .firstName(studentFromDatabase.getFirstName())
                .lastName(studentFromDatabase.getLastName())
                .dob(studentFromDatabase.getDob())
                .build();
    }
}
