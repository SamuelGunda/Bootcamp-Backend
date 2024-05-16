package com.kasv.gunda.bootcamp.repositories;

import com.kasv.gunda.bootcamp.models.StudentApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentApplicationRepository extends JpaRepository<StudentApplication, Integer> {

    StudentApplication getById(Long id);

}
