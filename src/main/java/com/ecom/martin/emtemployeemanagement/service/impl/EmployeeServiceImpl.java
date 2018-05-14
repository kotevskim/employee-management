package com.ecom.martin.emtemployeemanagement.service.impl;

import com.ecom.martin.emtemployeemanagement.model.Employee;
import com.ecom.martin.emtemployeemanagement.model.EmployeeEditObject;
import com.ecom.martin.emtemployeemanagement.model.EmployeeRegisterObject;
import com.ecom.martin.emtemployeemanagement.model.Role;
import com.ecom.martin.emtemployeemanagement.model.exception.DuplicateEmployeeException;
import com.ecom.martin.emtemployeemanagement.persistence.EmployeeDao;
import com.ecom.martin.emtemployeemanagement.service.EmployeeService;
import com.ecom.martin.emtemployeemanagement.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

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

    @Override
    @Transactional
    public Employee createEmployee(Employee e) {
        return this.employeeDao.save(e);
    }

    @Override
    public Page<Employee> getEmployees(Pageable pageable) {
        return this.employeeDao.findAll(pageable);
    }

    @Override
    public Optional<Employee> getEmployee(String email) {
        return this.employeeDao.findById(email);
    }

    @Override
    public void deleteEmployeeByEmail(String email) {
        this.employeeDao.deleteById(email);
    }

    @Override
    @Transactional
    public Employee editEmployee(Employee employee, EmployeeEditObject employeeEditObject) {
        employee.setFirstName(employeeEditObject.getFirstName());
        employee.setLastName(employeeEditObject.getLastName());
        employee.setBirthDate(employeeEditObject.getBirthDate());
        employee.setGender(employeeEditObject.getGender());
        return employee;
    }
}
