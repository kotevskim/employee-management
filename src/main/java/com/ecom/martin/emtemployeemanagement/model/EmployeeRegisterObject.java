package com.ecom.martin.emtemployeemanagement.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class EmployeeRegisterObject {

    private String email;
    private String password;
    private String matchingPassword;
    private String firstName;
    private String lastName;
    @DateTimeFormat(pattern = "yyyy-MM-dd") // to enable thymeleaf form binding with the model
    private LocalDate birthDate;
    private Gender gender;

    public EmployeeRegisterObject() {}

    public EmployeeRegisterObject(
            String email,
            String password,
            String matchingPassword,
            String firstName,
            String lastName,
            LocalDate birthDate,
            Gender gender) {
        this.email = email;
        this.password = password;
        this.matchingPassword = matchingPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
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

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
