package com.ecom.martin.emtemployeemanagement.persistence;

import com.ecom.martin.emtemployeemanagement.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeDao extends JpaRepository<Employee, String> {

    Optional<Employee> findByEmail(String email);

    Page<Employee> findByDepartmentId(Long id, Pageable pageable);

    Page<Employee> findAll(Pageable pageable);
}
