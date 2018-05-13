package com.ecom.martin.emtemployeemanagement.service.impl;

import com.ecom.martin.emtemployeemanagement.model.Department;
import com.ecom.martin.emtemployeemanagement.model.Employee;
import com.ecom.martin.emtemployeemanagement.persistence.DepartmentDao;
import com.ecom.martin.emtemployeemanagement.persistence.EmployeeDao;
import com.ecom.martin.emtemployeemanagement.service.DepartmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final EmployeeDao employeeDao;
    private final DepartmentDao departmentDao;

    public DepartmentServiceImpl(EmployeeDao employeeDao, DepartmentDao departmentDao) {
        this.employeeDao = employeeDao;
        this.departmentDao = departmentDao;
    }

    @Override
    public Optional<Department> getDepartment(Long id) {
        return this.departmentDao.findById(id);
    }

    @Override
    public Page<Employee> getEmployeesByDepartment(Long departmentId, Pageable pageable) {
        return this.employeeDao.findByDepartmentId(departmentId, pageable);
    }

    @Override
    public Optional<Department> getDepartmentByManager(String email) {
        return this.departmentDao.findByManagerEmail(email);
    }
}
