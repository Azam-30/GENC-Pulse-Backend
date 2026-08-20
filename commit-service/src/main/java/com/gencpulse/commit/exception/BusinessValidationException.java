package com.gencpulse.commit.exception;

public class BusinessValidationException
        extends RuntimeException {

    public BusinessValidationException(
            String message) {

        super(message);
    }
}