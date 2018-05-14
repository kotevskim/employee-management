package com.ecom.martin.emtemployeemanagement.web.controllers;

import com.ecom.martin.emtemployeemanagement.model.Employee;
import com.ecom.martin.emtemployeemanagement.model.EmployeeEditObject;
import com.ecom.martin.emtemployeemanagement.service.DepartmentService;
import com.ecom.martin.emtemployeemanagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Controller
public class EmployeeController {

    private final DepartmentService departmentService;
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(DepartmentService departmentService,
                              EmployeeService employeeService) {
        this.departmentService = departmentService;
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    public String getEmployeesOfDepartment(Model model,
                                           Pageable pageable,
                                           Sort sort) {
        Page<Employee> page = new PageImpl<>(Collections.emptyList());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>) authentication.getAuthorities();
        String role = authorities.get(0).getAuthority();
        if (role.equals("ROLE_ADMIN")) {
            page = this.employeeService.getEmployees(pageable);
        } else if (role.equals("ROLE_MANAGER")) {
            User user = (User) authentication.getPrincipal();
            page = this.departmentService.getDepartmentByManager(user.getUsername())
                    .map(d -> this.departmentService.getEmployeesByDepartment(d.getId(), pageable))
                    .orElse(new PageImpl<>(Collections.emptyList()));
        }

        model.addAttribute("email", getActiveUser().getUsername());
        model.addAttribute("employees", page.getContent());
        model.addAttribute("totalElements", page.getTotalElements());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("pageNumber", page.getNumber());
        model.addAttribute("hasNextPage", page.hasNext());
        model.addAttribute("hasPrevPage", page.hasPrevious());

        return "employees-mgmt";
    }

    @DeleteMapping("/employees/{email}")
    public String test(@PathVariable String email,
                       Pageable pageable,
                       Sort sort,
                       Model model) {
        this.employeeService.deleteEmployeeByEmail(email);
        return getEmployeesOfDepartment(model, pageable, sort);
    }

    @GetMapping("/profile-edit")
    public String showEditProfilePage(Model model) {
        String username =
                ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Employee u = this.employeeService.getEmployee(username).get();
        EmployeeEditObject employeeEditObject
                = new EmployeeEditObject(u.getFirstName(), u.getLastName(), u.getBirthDate(), u.getGender());
        model.addAttribute("email", getActiveUser().getUsername());
        model.addAttribute("user", employeeEditObject);
        return "profile-edit";
    }

    @PostMapping("/profile-edit")
    public String editProfile(Model model,
                              @ModelAttribute("user") @Valid EmployeeEditObject employeeEditObject,
                              BindingResult bindingResult) {
        String username =
                ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Employee e = this.employeeService.getEmployee(username).get();
        e = this.employeeService.editEmployee(e, employeeEditObject);
        model.addAttribute("email", getActiveUser().getUsername());
        return "redirect:/me";
    }


    private User getActiveUser() {
        return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
}
