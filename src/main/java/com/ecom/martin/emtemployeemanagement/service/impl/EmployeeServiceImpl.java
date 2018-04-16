package com.ecom.martin.emtemployeemanagement.service.impl;

import com.ecom.martin.emtemployeemanagement.model.Employee;
import com.ecom.martin.emtemployeemanagement.model.EmployeeRegisterObject;
import com.ecom.martin.emtemployeemanagement.model.Role;
import com.ecom.martin.emtemployeemanagement.model.exception.DuplicateEmployeeException;
import com.ecom.martin.emtemployeemanagement.model.exception.PasswordMismatchException;
import com.ecom.martin.emtemployeemanagement.persistence.EmployeeDao;
import com.ecom.martin.emtemployeemanagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final PasswordEncoder passwordEncoder;
    private final EmployeeDao employeeDao;

    @Autowired
    public EmployeeServiceImpl(PasswordEncoder passwordEncoder, EmployeeDao employeeDao) {
        this.passwordEncoder = passwordEncoder;
        this.employeeDao = employeeDao;
    }

    @Override
    public void createEmployee(EmployeeRegisterObject employee) {
        if (this.employeeDao.findByEmail(employee.getEmail()).isPresent()) {
            throw new DuplicateEmployeeException();
        }
        // TODO refactor this, use validator
        if (!employee.getPassword().equals(employee.getMatchingPassword())) {
            throw new PasswordMismatchException();
        }
        final boolean activated = false;
        final LocalDateTime registrationDate = LocalDateTime.now();
        final String activationCode = this.generateActivationCode(18);
        final Role role = Role.EMPLOYEE;
        this.employeeDao.save(new Employee(
                employee.getEmail(),
                this.passwordEncoder.encode(employee.getPassword()),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getGender(),
                null,
                employee.getBirthDate(),
                activated,
                registrationDate,
                activationCode,
                role
        ));
    }


    private static String generateActivationCode(int count) {
        final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
}
