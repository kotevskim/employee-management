package com.ecom.martin.emtemployeemanagement.service;

import com.ecom.martin.emtemployeemanagement.model.Employee;
import com.ecom.martin.emtemployeemanagement.persistence.EmployeeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private EmployeeDao employeeDao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee employee = this.employeeDao.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Employee with email: " + email + " does not exist"));

        return new User(
                employee.getEmail(),
                employee.getPassword(),
                Arrays.asList(new SimpleGrantedAuthority(employee.getRole().toString()))
                );
    }
}
