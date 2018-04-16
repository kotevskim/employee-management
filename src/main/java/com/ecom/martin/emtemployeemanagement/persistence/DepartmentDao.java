package com.ecom.martin.emtemployeemanagement.persistence;

import com.ecom.martin.emtemployeemanagement.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentDao extends JpaRepository<Department, Long> {
}
