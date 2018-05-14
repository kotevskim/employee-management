package com.ecom.martin.emtemployeemanagement.config;

import com.ecom.martin.emtemployeemanagement.model.Employee;
import com.ecom.martin.emtemployeemanagement.model.Role;
import com.ecom.martin.emtemployeemanagement.service.EmployeeService;
import com.ecom.martin.emtemployeemanagement.service.SsoIdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Component
public class OAuth2SeccessHandler implements AuthenticationSuccessHandler {

    private final SsoIdentityService ssoIdentityService;
    private final EmployeeService employeeService;

    @Autowired
    public OAuth2SeccessHandler(SsoIdentityService ssoIdentityService,
                                EmployeeService employeeService) {
        this.ssoIdentityService = ssoIdentityService;
        this.employeeService = employeeService;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
        Map<String, String> details
                = (Map<String, String>) oAuth2Authentication.getUserAuthentication().getDetails();
        // TODO this is only for github, enable for other too
        String ssoId = String.valueOf(details.getOrDefault("id", ""));
        String email = details.getOrDefault("email", "");
        String name = details.getOrDefault("name", "");
        this.ssoIdentityService.findEmployee(ssoId)
                .orElseGet(() -> {
                    Employee e = this.employeeService.createEmployee(new Employee(email,
                            null,
                            name,
                            null,
                            null,
                            null,
                            null,
                            LocalDateTime.now(),
                            Role.ROLE_EMPLOYEE,
                            true,
                            true,
                            true,
                            true));
                    this.ssoIdentityService.createSsoIdentity(ssoId, e);
                    return e;
                });
        response.sendRedirect("http://localhost:8080/me");
    }
}
