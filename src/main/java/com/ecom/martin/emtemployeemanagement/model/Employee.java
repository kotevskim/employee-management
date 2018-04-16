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

    private boolean activated;

    private LocalDateTime registrationDate;

    private String activationCode;

    private Role role;

    // JPA only
    public Employee() {}

    public Employee(
            String email,
            String password,
            String firstName,
            String lastName,
            Gender gender,
            Department department,
            LocalDate birthDate,
            boolean activated,
            LocalDateTime registrationDate,
            String activationCode,
            Role role) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.department = department;
        this.birthDate = birthDate;
        this.activated = activated;
        this.registrationDate = registrationDate;
        this.activationCode = activationCode;
        this.role = role;
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

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
