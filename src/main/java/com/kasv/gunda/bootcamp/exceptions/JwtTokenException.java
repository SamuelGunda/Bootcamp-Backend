package com.kasv.gunda.bootcamp.exceptions;

public class JwtTokenException extends RuntimeException {

    public JwtTokenException(String message) {
        super(message);
    }
}