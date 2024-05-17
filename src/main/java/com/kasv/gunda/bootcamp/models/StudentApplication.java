package com.kasv.gunda.bootcamp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@Table(name = "student_applications")
public class StudentApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String firstName;

    private String lastName;

    @Temporal(TemporalType.DATE)
    private Date dob;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    public StudentApplication(User user, String firstName, String lastName, Date dob) {
        this.userId = user.getId();
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
    }

    public StudentApplication() {}
}
