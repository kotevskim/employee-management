package com.ecom.martin.emtemployeemanagement.config;


import com.ecom.martin.emtemployeemanagement.model.Employee;
import com.ecom.martin.emtemployeemanagement.persistence.EmployeeDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final EmployeeDao employeeDao;

    public SecurityConfiguration(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth
                .userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin().loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/me", true)
//                .successForwardUrl("/profile-edit")
                .and()
                .logout().clearAuthentication(true).invalidateHttpSession(true).deleteCookies("remember-me")
                .and()
                .rememberMe().key("myRememberMeKey").tokenValiditySeconds(2592000)
                .and()
                .authorizeRequests()
                .antMatchers("/me", "/profile-edit", "/password-change").authenticated()
                .antMatchers("/employees").hasAnyRole("MANAGER", "ADMIN")
                .and()
                .authorizeRequests().antMatchers(
                "/login",
                "/register",
                "/activation",
                "/password-reset").permitAll();
        // for a REST API
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder;
//        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return (String email) -> {
            Employee employee = employeeDao
                    .findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Employee with email: " + email + " does not exist"));

            return new User(
                    employee.getEmail(),
                    employee.getPassword(),
                    employee.isEnabled(),
                    employee.isAccountNotExpired(),
                    employee.isCredentialsNotExpired(),
                    employee.isAccountNotLocked(),
                    Arrays.asList(new SimpleGrantedAuthority(employee.getRole().toString())));
        };
    }

}
