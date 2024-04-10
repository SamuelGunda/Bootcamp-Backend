package com.kasv.gunda.bootcamp.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class LogoutRequest {
    private String token;
    private String username;
    private String lastName;


}
