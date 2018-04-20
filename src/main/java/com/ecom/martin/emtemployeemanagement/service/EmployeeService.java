package com.ecom.martin.emtemployeemanagement.service;

import com.ecom.martin.emtemployeemanagement.model.Employee;
import com.ecom.martin.emtemployeemanagement.model.EmployeeRegisterObject;

public interface EmployeeService {

    Employee createEmployee(EmployeeRegisterObject user);
}
