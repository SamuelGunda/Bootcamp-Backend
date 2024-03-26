package com.kasv.gunda.bootcamp.repositories;


import com.kasv.gunda.bootcamp.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer>{

    List<Student> findAll();

}
