package com.ecom.martin.emtemployeemanagement.model;

import com.ecom.martin.emtemployeemanagement.web.validators.FieldsValueMatch;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@FieldsValueMatch.List({
        @FieldsValueMatch(
                message = "Passwords do not match",
                field = "password",
                fieldMatch = "verifyPassword"
        )
})
public class EmployeeRegisterObject {

    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
    @NotEmpty
    private String verifyPassword;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @DateTimeFormat(pattern = "yyyy-MM-dd") // to enable thymeleaf form binding with the model
    private LocalDate birthDate;
    private Gender gender;

    public EmployeeRegisterObject() {}

    public EmployeeRegisterObject(
            String email,
            String password,
            String verifyPassword,
            String firstName,
            String lastName,
            LocalDate birthDate,
            Gender gender) {
        this.email = email;
        this.password = password;
        this.verifyPassword = verifyPassword;
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

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
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
