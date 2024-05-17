package com.kasv.gunda.bootcamp.payload.response;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {
    private long id;
    private String firstName;
    private String lastName;
    private Date dob;

    public StudentResponse(long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
