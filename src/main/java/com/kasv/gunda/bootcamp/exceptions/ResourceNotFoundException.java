package com.kasv.gunda.bootcamp.exceptions;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super("Resource not found: " + message);
    }
}
