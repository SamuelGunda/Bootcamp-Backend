package com.kasv.gunda.bootcamp.repositories;

import com.kasv.gunda.bootcamp.models.Status;
import com.kasv.gunda.bootcamp.models.StudentRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRegistrationRepository extends JpaRepository<StudentRegistration, Integer> {

}
