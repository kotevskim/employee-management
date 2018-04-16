package com.ecom.martin.emtemployeemanagement.web.controller;

import com.ecom.martin.emtemployeemanagement.model.EmployeeRegisterObject;
import com.ecom.martin.emtemployeemanagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LandingController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping(value = "me")
    public String getMe() {
        return "me";
    }

    @GetMapping(value = "login")
    public String login() {
        return "login";
    }

    @GetMapping(value = "register")
    public String showRegistrationPage(Model model) {
        EmployeeRegisterObject employeeRegisterObject = new EmployeeRegisterObject();
        model.addAttribute("employee", employeeRegisterObject);
        return "register";
    }

    @PostMapping(value = "register")
    public String registerNewEmployee(
            @ModelAttribute("employee") EmployeeRegisterObject employeeRegisterObject,
            BindingResult bindingResult) {
        this.employeeService.createEmployee(employeeRegisterObject);
        return "register";
    }
}
