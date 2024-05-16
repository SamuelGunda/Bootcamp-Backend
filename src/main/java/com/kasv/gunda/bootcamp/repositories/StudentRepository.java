package com.kasv.gunda.bootcamp.repositories;


import com.kasv.gunda.bootcamp.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer>{

    List<Student> findAll();

    boolean existsById(long id);

    Student findById(long id);

}
