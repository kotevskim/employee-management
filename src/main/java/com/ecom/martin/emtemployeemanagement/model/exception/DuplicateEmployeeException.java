package com.ecom.martin.emtemployeemanagement.model.exception;

public class DuplicateEmployeeException extends RuntimeException {

    public DuplicateEmployeeException() {
        super();
    }

    public DuplicateEmployeeException(String message) {
        super(message);
    }
}
