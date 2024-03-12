package com.kasv.gunda.bootcamp.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "user")
public class user {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    public user(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public user() {
    }
}
