package com.ecom.martin.emtemployeemanagement.service.impl;

import com.ecom.martin.emtemployeemanagement.model.Employee;
import com.ecom.martin.emtemployeemanagement.model.EmployeeRegisterObject;
import com.ecom.martin.emtemployeemanagement.model.Role;
import com.ecom.martin.emtemployeemanagement.model.exception.DuplicateEmployeeException;
import com.ecom.martin.emtemployeemanagement.persistence.EmployeeDao;
import com.ecom.martin.emtemployeemanagement.service.EmployeeService;
import com.ecom.martin.emtemployeemanagement.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final PasswordEncoder passwordEncoder;
    private final EmployeeDao employeeDao;

    @Autowired
    public EmployeeServiceImpl(PasswordEncoder passwordEncoder, EmployeeDao employeeDao, MailService mailService) {
        this.passwordEncoder = passwordEncoder;
        this.employeeDao = employeeDao;
    }

    @Override
    public Employee createEmployee(EmployeeRegisterObject employee) {
        employeeDao.findByEmail(employee.getEmail()).ifPresent(it -> {throw new DuplicateEmployeeException();});
        final LocalDateTime registrationDate = LocalDateTime.now();
        final Role role = Role.ROLE_EMPLOYEE;
        final boolean enabled = false;
        final boolean accountNotExpired = true;
        final boolean credentialsNotExpired = true;
        final boolean accountNotLocked = true;
        Employee em = new Employee(
                employee.getEmail(),
                this.passwordEncoder.encode(employee.getPassword()),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getGender(),
                null,
                employee.getBirthDate(),
                registrationDate,
                role,
                enabled,
                accountNotExpired,
                credentialsNotExpired,
                accountNotLocked

        );
        this.employeeDao.save(em);
        return em;
    }
}
