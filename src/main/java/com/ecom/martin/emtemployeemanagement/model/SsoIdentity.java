package com.ecom.martin.emtemployeemanagement.model;

import javax.persistence.*;
import javax.transaction.Transactional;

@Entity
@Table(name = "sso_identities")
public class SsoIdentity {

    @Id
    String ssoId;
    @OneToOne
    @JoinColumn(name = "user_id")
    Employee employee;

    // JPA only
    public SsoIdentity() {
    }

    public SsoIdentity(String ssoId, Employee employee) {
        this.ssoId = ssoId;
        this.employee = employee;
    }

    public String getSsoId() {
        return ssoId;
    }

    public void setSsoId(String ssoId) {
        this.ssoId = ssoId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
