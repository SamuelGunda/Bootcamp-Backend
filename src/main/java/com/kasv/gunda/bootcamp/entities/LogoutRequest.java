package com.kasv.gunda.bootcamp.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogoutRequest {
    private String token;
    private String username;
}
