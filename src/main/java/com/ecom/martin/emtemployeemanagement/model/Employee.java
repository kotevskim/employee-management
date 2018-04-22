package com.ecom.martin.emtemployeemanagement.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "EMPLOYEE") // TODO why doesn't take this into consideration, table names are lowercase?
public class Employee {

    @Id
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Gender gender;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
    private LocalDate birthDate;
    private LocalDateTime registrationDate;
    private Role role;
    private boolean enabled;
    private boolean accountNotExpired;
    private boolean credentialsNotExpired;
    private boolean accountNotLocked;

    // JPA only
    public Employee() {}

    public Employee(String email,
                    String password,
                    String firstName,
                    String lastName,
                    Gender gender,
                    Department department,
                    LocalDate birthDate,
                    LocalDateTime registrationDate,
                    Role role,
                    boolean enabled,
                    boolean accountNotExpired,
                    boolean credentialsNotExpired,
                    boolean accountNotLocked) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.department = department;
        this.birthDate = birthDate;
        this.registrationDate = registrationDate;
        this.role = role;
        this.enabled = enabled;
        this.accountNotExpired = accountNotExpired;
        this.credentialsNotExpired = credentialsNotExpired;
        this.accountNotLocked = accountNotLocked;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAccountNotExpired() {
        return accountNotExpired;
    }

    public void setAccountNotExpired(boolean accountNotExpired) {
        this.accountNotExpired = accountNotExpired;
    }

    public boolean isCredentialsNotExpired() {
        return credentialsNotExpired;
    }

    public void setCredentialsNotExpired(boolean credentialsNotExpired) {
        this.credentialsNotExpired = credentialsNotExpired;
    }

    public boolean isAccountNotLocked() {
        return accountNotLocked;
    }

    public void setAccountNotLocked(boolean accountNotLocked) {
        this.accountNotLocked = accountNotLocked;
    }
}
