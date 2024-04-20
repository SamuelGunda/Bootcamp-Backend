package com.kasv.gunda.bootcamp.repositories;

import com.kasv.gunda.bootcamp.entities.Student;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class StudentRepositoryTests {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void StudentRepository_SaveAll_ReturnSavedStudents() {

        // Arrange
        Student student = Student.builder()
                .firstName("Branči")
                .lastName("Kováč")
                .dob(new Date()).build();

        // Act
        Student savedStudent = studentRepository.save(student);

        // Assert
        Assertions.assertThat(savedStudent).isNotNull();
        Assertions.assertThat(savedStudent.getId()).isGreaterThan(0);
    }

    @Test
    public void findAll_ReturnsAllStudents() {
        // Arrange
        Student student1 = Student.builder().firstName("John").lastName("Doe").dob(new Date()).build();
        Student student2 = Student.builder().firstName("Jane").lastName("Doe").dob(new Date()).build();
        studentRepository.save(student1);
        studentRepository.save(student2);

        // Act
        List<Student> students = studentRepository.findAll();

        // Assert
        Assertions.assertThat(students).hasSize(2);
    }

    @Test
    public void existsById_ReturnsTrue_WhenStudentExists() {
        // Arrange
        Student student = Student.builder().firstName("John").lastName("Doe").dob(new Date()).build();
        Student savedStudent = studentRepository.save(student);

        // Act
        boolean exists = studentRepository.existsById(savedStudent.getId());

        // Assert
        Assertions.assertThat(exists).isTrue();
    }

    @Test
    public void existsById_ReturnsFalse_WhenStudentDoesNotExist() {
        // Act
        boolean exists = studentRepository.existsById(999L); // Assuming this ID does not exist

        // Assert
        Assertions.assertThat(exists).isFalse();
    }

    @Test
    public void findById_ReturnsStudent_WhenStudentExists() {
        // Arrange
        Student student = Student.builder().firstName("John").lastName("Doe").dob(new Date()).build();
        Student savedStudent = studentRepository.save(student);

        // Act
        Student foundStudent = studentRepository.findById(savedStudent.getId());

        // Assert
        Assertions.assertThat(foundStudent).isEqualTo(savedStudent);
    }

    @Test
    public void findById_ReturnsNull_WhenStudentDoesNotExist() {
        // Act
        Student foundStudent = studentRepository.findById(999L); // Assuming this ID does not exist

        // Assert
        Assertions.assertThat(foundStudent).isNull();
    }
}
