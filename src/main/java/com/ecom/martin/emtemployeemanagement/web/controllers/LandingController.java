package com.ecom.martin.emtemployeemanagement.web.controllers;

import com.ecom.martin.emtemployeemanagement.model.EmployeeRegisterObject;
import com.ecom.martin.emtemployeemanagement.service.AuthService;
import com.ecom.martin.emtemployeemanagement.service.EmployeeService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
public class LandingController {

    private final EmployeeService employeeService;
    private final AuthService authService;

    public LandingController(EmployeeService employeeService, AuthService authService) {
        this.employeeService = employeeService;
        this.authService = authService;
    }

    @GetMapping(value = "me")
    public String getMe(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        model.addAttribute("username", username);
        return "me";
    }

    @GetMapping(value = "activation")
    public String showActivationPage() {
        return "activation";
    }

    @GetMapping(value = "login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping(value = "register")
    public String showRegistrationPage(Model model) {
        EmployeeRegisterObject employeeRegisterObject = new EmployeeRegisterObject();
        model.addAttribute("employee", employeeRegisterObject);
        return "register";
    }

    @PostMapping(value = "register")
    public ModelAndView registerEmployee(
            @ModelAttribute("employee") @Valid final EmployeeRegisterObject employeeRegisterObject,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("objectErrors", bindingResult.getAllErrors().stream().filter(err -> err.getClass() != FieldError.class).collect(Collectors.toList()));
            model.addAttribute("fieldErrors", bindingResult.getFieldErrors());
            model.addAttribute("errorPasswordsDoNotMatch", "Passwords do not match!");
            return new ModelAndView("register?error");
        }
        this.authService.registerEmployee(employeeRegisterObject);
        return new ModelAndView("redirect:/activation");
    }

    @PostMapping(value = "activation")
    public ModelAndView activateEmployee(@RequestParam String activationCode, Model model) {
        try {
            this.authService.verifyEmployee(activationCode);
            return new ModelAndView("redirect:/login");
        } catch (RuntimeException e) {
            return new ModelAndView("redirect:/activation?error");
        }
    }

    @PostMapping("edit-profile")
    public String showEditProfilePage(@RequestParam String email, Model model) {
        model.addAttribute("email", email);
        return "edit-profile";
    }


}
