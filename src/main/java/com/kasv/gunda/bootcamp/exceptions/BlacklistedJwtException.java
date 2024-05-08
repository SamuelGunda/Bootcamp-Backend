package com.kasv.gunda.bootcamp.exceptions;

public class BlacklistedJwtException extends RuntimeException {

    public BlacklistedJwtException(String message) {
        super(message);
    }
}
