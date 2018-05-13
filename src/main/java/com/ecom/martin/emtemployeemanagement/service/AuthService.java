package com.ecom.martin.emtemployeemanagement.service;

import com.ecom.martin.emtemployeemanagement.model.Employee;
import com.ecom.martin.emtemployeemanagement.model.EmployeeRegisterObject;
import com.ecom.martin.emtemployeemanagement.model.EmployeeVerificationToken;
import com.ecom.martin.emtemployeemanagement.service.impl.AuthServiceImpl;

public interface AuthService {

    void registerEmployee(EmployeeRegisterObject employee);

    void verifyEmployee(String activationCode);

    void resetPasswordForEmployee(Employee employee);

}
