package com.ecom.martin.emtemployeemanagement.service;

import com.ecom.martin.emtemployeemanagement.model.EmployeeRegisterObject;
import com.ecom.martin.emtemployeemanagement.model.EmployeeVerificationToken;
import com.ecom.martin.emtemployeemanagement.service.impl.AuthServiceImpl;

public interface AuthService {

    public void registerEmployee(EmployeeRegisterObject employee);

    public void verifyEmployee(String activationCode);
}
