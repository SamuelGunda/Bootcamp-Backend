package com.kasv.gunda.bootcamp.exceptions;

public class InvalidActionException extends RuntimeException {

    public InvalidActionException(String message) {
        super("Invalid action: " + message);
    }
}
