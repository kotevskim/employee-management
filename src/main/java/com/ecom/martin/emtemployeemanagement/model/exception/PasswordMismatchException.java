package com.ecom.martin.emtemployeemanagement.model.exception;

public class PasswordMismatchException extends RuntimeException {

    public PasswordMismatchException() {
    }

    public PasswordMismatchException(String message) {
        super(message);
    }
}
