package com.kasv.gunda.bootcamp.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String token;
    private String username;
}
