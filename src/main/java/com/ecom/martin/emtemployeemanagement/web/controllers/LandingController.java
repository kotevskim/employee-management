package com.ecom.martin.emtemployeemanagement.web.controllers;

import com.ecom.martin.emtemployeemanagement.model.Employee;
import com.ecom.martin.emtemployeemanagement.model.EmployeeRegisterObject;
import com.ecom.martin.emtemployeemanagement.service.AuthService;
import com.ecom.martin.emtemployeemanagement.service.EmployeeService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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
    private final PasswordEncoder passwordEncoder;

    public LandingController(EmployeeService employeeService,
                             AuthService authService,
                             PasswordEncoder passwordEncoder) {
        this.employeeService = employeeService;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(value = "/me")
    public String getMe(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails)principal).getUsername();
        } else {
            email = principal.toString();
        }
        model.addAttribute("email", email);
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
    public String registerEmployee(
            @ModelAttribute("employee") @Valid final EmployeeRegisterObject employeeRegisterObject,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("objectErrors", bindingResult.getAllErrors().stream().filter(err -> err.getClass() != FieldError.class).collect(Collectors.toList()));
            model.addAttribute("fieldErrors", bindingResult.getFieldErrors());
            model.addAttribute("errorPasswordsDoNotMatch", "Passwords do not match!");
            return "register";
        }
        this.authService.registerEmployee(employeeRegisterObject);
        return "redirect:/activation";
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

    @GetMapping("/password-reset")
    public String showForgotPasswordPage() {
        return "password-reset";
    }

    @PostMapping("/password-reset")
    public String resetPassword(@RequestParam String email) {
        return this.employeeService.getEmployee(email)
                .map(e -> {
                    this.authService.resetPasswordForEmployee(e);
                    return "redirect:/login";
                })
                .orElse("redirect:/password-reset?error");
    }

    @GetMapping("/password-change")
    public String showChangePasswordPage(Model model) {
        model.addAttribute("email", getActiveUser().getUsername());
        return "password-change";
    }

    @PostMapping("/password-change")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String newPasswordRepeat
    ) {
        Employee e = employeeService.getEmployee(getActiveUser().getUsername()).get();
        boolean validPassword = authService.isValidPassword(e, oldPassword);
        if (!validPassword) {
            return "redirect:/password-change?invalid";
        } else if (!newPassword.equals(newPasswordRepeat)) {
            return "redirect:/password-change?dontMatch";
        } else {
            this.authService.updatePassword(e, newPassword);
            return "redirect:/me"; // TODO should logout after password change
        }
    }

    private User getActiveUser() {
        return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

}
