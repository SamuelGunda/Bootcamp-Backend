package com.kasv.gunda.bootcamp.services;


import com.kasv.gunda.bootcamp.exceptions.ApplicationAlreadyProcessedException;
import com.kasv.gunda.bootcamp.exceptions.InvalidActionException;
import com.kasv.gunda.bootcamp.exceptions.ResourceNotFoundException;
import com.kasv.gunda.bootcamp.exceptions.UnauthorizedAccessException;
import com.kasv.gunda.bootcamp.models.Status;
import com.kasv.gunda.bootcamp.models.StudentApplication;
import com.kasv.gunda.bootcamp.repositories.StudentApplicationRepository;
import com.kasv.gunda.bootcamp.security.jwt.JwtUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentApplicationService {

    private final StudentApplicationRepository studentApplicationRepository;
    private final StudentService studentService;

    private final JwtUtils jwtUtils;

    public StudentApplicationService(StudentApplicationRepository studentApplicationRepository, StudentService studentService, JwtUtils jwtUtils) {
        this.studentApplicationRepository = studentApplicationRepository;
        this.studentService = studentService;
        this.jwtUtils = jwtUtils;
    }

    public List<StudentApplication> getAllApplications() {
        try {
            return studentApplicationRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public StudentApplication getApplicationById(Long id) {
        try {
            return studentApplicationRepository.getById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void createApplication(StudentApplication application, String authorizationHeader) {

        if (application == null) {
            throw new IllegalArgumentException("Application cannot be null.");
        }
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            long usersId = jwtUtils.getUserIdFromJwtToken(token);

            if (usersId != application.getUserId()) {
                throw new UnauthorizedAccessException("You are not authorized to perform this action.");
            }

            try {
                studentApplicationRepository.save(application);
                return;
            } catch (Exception e) {
                throw new RuntimeException("Error: " + e.getMessage());
            }
        }
        throw new UnauthorizedAccessException("You are not authorized to perform this action.");
    }

    public StudentApplication updateApplication(Long id, String action) {
        StudentApplication application = studentApplicationRepository.getById(id);
        if (application == null) {
            throw new ResourceNotFoundException("Application with id " + id + " not found.");
        }
        if (application.getStatus() != Status.PENDING) {
            throw new ApplicationAlreadyProcessedException();
        }

        if (action.equals("accept")) {
            application.setStatus(Status.ACCEPTED);
            studentService.registerStudent(application);
        } else if (action.equals("decline")) {
            application.setStatus(Status.DECLINED);
        } else {
            throw new InvalidActionException(action);
        }

        studentApplicationRepository.save(application);
        return application;
    }
}
