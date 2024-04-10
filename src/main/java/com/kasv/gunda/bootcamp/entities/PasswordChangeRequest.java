package com.kasv.gunda.bootcamp.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeRequest {
    private String token;
    private String newPassword;
    private String oldPassword;
}
