package com.ecom.martin.emtemployeemanagement.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

public class EmployeeEditObject {

    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @DateTimeFormat(pattern = "yyyy-MM-dd") // to enable thymeleaf form binding with the model
    private LocalDate birthDate;
    private Gender gender;

    public EmployeeEditObject() {
    }

    public EmployeeEditObject(@NotEmpty String firstName,
                              @NotEmpty String lastName,
                              LocalDate birthDate,
                              Gender gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
