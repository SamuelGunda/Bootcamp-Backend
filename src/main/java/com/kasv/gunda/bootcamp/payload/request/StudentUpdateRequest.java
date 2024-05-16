package com.kasv.gunda.bootcamp.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class StudentUpdateRequest {
    private String firstName;
    private String lastName;
    private Date dob;
}
