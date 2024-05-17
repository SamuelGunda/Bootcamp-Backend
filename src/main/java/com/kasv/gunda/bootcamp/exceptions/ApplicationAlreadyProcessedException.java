package com.kasv.gunda.bootcamp.exceptions;

public class ApplicationAlreadyProcessedException extends RuntimeException {

    public ApplicationAlreadyProcessedException() {
        super("Application already accepted/declined");
    }
}
