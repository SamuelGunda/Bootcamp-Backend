package com.kasv.gunda.bootcamp.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;

    @Temporal(TemporalType.DATE)
    private Date dob;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
