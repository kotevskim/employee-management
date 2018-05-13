package com.ecom.martin.emtemployeemanagement.model.exception;

public class DepartmentNotFoundException extends RuntimeException {

    public DepartmentNotFoundException(Long id) {
        super("Department with id " + id + " does not exist");
    }
}
