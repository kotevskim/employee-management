package com.ecom.martin.emtemployeemanagement.persistence;

import com.ecom.martin.emtemployeemanagement.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeDao extends JpaRepository<Employee, String> {

    Optional<Employee> findByEmail(String email);
}
