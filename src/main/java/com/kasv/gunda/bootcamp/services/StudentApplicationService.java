package com.kasv.gunda.bootcamp.services;

import com.google.gson.Gson;
import com.kasv.gunda.bootcamp.models.StudentApplication;
import com.kasv.gunda.bootcamp.repositories.StudentApplicationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentApplicationService {

    private final StudentApplicationRepository studentApplicationRepository;

    public StudentApplicationService(StudentApplicationRepository studentApplicationRepository) {
        this.studentApplicationRepository = studentApplicationRepository;
    }

    public String getAllApplications() {
        Gson gson = new Gson();
        List<StudentApplication> applications = studentApplicationRepository.findAll();
        return gson.toJson(applications);
    }

    public ResponseEntity<String> getApplicationById(Long id) {
        Gson gson = new Gson();
        StudentApplication application = studentApplicationRepository.getById(id);
        return ResponseEntity.ok(gson.toJson(application));
    }
}
