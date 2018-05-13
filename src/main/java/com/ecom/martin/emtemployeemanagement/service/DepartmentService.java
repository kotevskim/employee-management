package com.ecom.martin.emtemployeemanagement.service;

import com.ecom.martin.emtemployeemanagement.model.Department;
import com.ecom.martin.emtemployeemanagement.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DepartmentService {

    Optional<Department> getDepartment(Long id);

    Page<Employee> getEmployeesByDepartment(Long departmentId, Pageable pageable);

    Optional<Department> getDepartmentByManager(String email);
}
