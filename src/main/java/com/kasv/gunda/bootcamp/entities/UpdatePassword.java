package com.kasv.gunda.bootcamp.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePassword {

    private String token;
    private String username;
    private String newPassword;
    private String oldPassword;

}
