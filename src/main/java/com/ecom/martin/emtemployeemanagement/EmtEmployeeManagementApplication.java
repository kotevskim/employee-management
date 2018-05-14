package com.ecom.martin.emtemployeemanagement;

import com.ecom.martin.emtemployeemanagement.model.Employee;
import com.ecom.martin.emtemployeemanagement.model.Role;
import com.ecom.martin.emtemployeemanagement.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@SpringBootApplication
@EnableAsync
public class EmtEmployeeManagementApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmtEmployeeManagementApplication.class);

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Value("${app.user.admin.email}")
    private String adminEmail;

    @Value("${app.user.admin.password}")
    private String adminPassword;

    public static void main(String[] args) {
        SpringApplication.run(EmtEmployeeManagementApplication.class, args);
    }

    @PostConstruct
    public void init() {
        LOGGER.info("Initializing admin user");
//        user.email = env.getProperty("app.user.admin.email");
//        user.username = env.getProperty("app.user.admin.username");
//		if (employeeService.numberOfUsers() == 0) {
        Employee admin = new Employee(
                adminEmail,
                passwordEncoder.encode(adminPassword),
                null,
                null,
                null,
                null,
                null,
                LocalDateTime.now(),
                Role.ROLE_ADMIN,
                true,
                true,
                true,
                true
        );
        this.employeeService.createEmployee(admin);
//		}
    }
}
