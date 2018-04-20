package com.ecom.martin.emtemployeemanagement.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "employee_verification_tokens")
public class EmployeeVerificationToken {
    @Id
    private String code;
    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    private LocalDateTime expirationDate;

    public EmployeeVerificationToken() {
        this.code =  UUID.randomUUID().toString();
    }

    public EmployeeVerificationToken(Employee employee) {
        this.code =  UUID.randomUUID().toString();
        this.employee = employee;
        this.expirationDate = LocalDateTime.now().plusDays(2L);
    }

    public String getCode() {
        return code;
    }

    public Employee getEmployee() {
        return employee;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }
}
