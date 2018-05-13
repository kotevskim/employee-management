package com.ecom.martin.emtemployeemanagement.service;

import com.ecom.martin.emtemployeemanagement.model.Employee;
import com.ecom.martin.emtemployeemanagement.model.EmployeeEditObject;
import com.ecom.martin.emtemployeemanagement.model.EmployeeRegisterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EmployeeService {

    Employee createEmployee(EmployeeRegisterObject user);

    Page<Employee> getEmployees(Pageable pageable);

    Optional<Employee> getEmployee(String email);

    void deleteEmployeeByEmail(String email);

    Employee editEmployee(Employee employee, EmployeeEditObject employeeEditObject);
}
