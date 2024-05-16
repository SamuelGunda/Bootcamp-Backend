package com.kasv.gunda.bootcamp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "student_registrations")
public class StudentRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String firstName;

    private String lastName;

    private Date dob;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    public StudentRegistration(User user, String firstName, String lastName, Date dob) {
        this.userId = user.getId();
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
    }

    public StudentRegistration() {}
}
