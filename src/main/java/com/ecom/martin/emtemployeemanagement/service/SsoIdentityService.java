package com.ecom.martin.emtemployeemanagement.service;

import com.ecom.martin.emtemployeemanagement.model.Employee;
import com.ecom.martin.emtemployeemanagement.model.SsoIdentity;

import java.util.Optional;

public interface SsoIdentityService {

    Optional<Employee> findEmployee(String ssoId);

    SsoIdentity createSsoIdentity(String ssoId, Employee e);
}
