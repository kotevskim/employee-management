package com.ecom.martin.emtemployeemanagement.config;


import com.ecom.martin.emtemployeemanagement.model.Employee;
import com.ecom.martin.emtemployeemanagement.persistence.EmployeeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CompositeFilter;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableOAuth2Client
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {



    private final OAuth2ClientContext oauth2ClientContext;
    private final EmployeeDao employeeDao;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfiguration(@Qualifier("oauth2ClientContext") OAuth2ClientContext oauth2ClientContext,
                                 EmployeeDao employeeDao,
                                 AuthenticationSuccessHandler authenticationSuccessHandler,
                                 PasswordEncoder passwordEncoder) {
        this.oauth2ClientContext = oauth2ClientContext;
        this.employeeDao = employeeDao;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth
                .userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        String[] endpointsPublic = new String[]{
                "/login",
                "/register",
                "/activation",
                "/password-reset"
        };

        String[] endpointsAuthenticated = new String[]{
                "/me",
                "/profile-edit",
                "/password-change"
        };

        http
                .formLogin().loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/me", true)
                .and()
                .logout().clearAuthentication(true).invalidateHttpSession(true).deleteCookies("remember-me")
                .and()
                .rememberMe().key("myRememberMeKey").tokenValiditySeconds(2592000)
                .and()
                .authorizeRequests()
                .antMatchers(endpointsPublic).permitAll()
                .antMatchers(endpointsAuthenticated).authenticated()
                .antMatchers("/employees/**").hasAnyRole("MANAGER", "ADMIN")
                .and()
                .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
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

    @Bean
    public FilterRegistrationBean<OAuth2ClientContextFilter> oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
        FilterRegistrationBean<OAuth2ClientContextFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }

    @Bean
    @ConfigurationProperties("github")
    public ClientResources github() {
        return new ClientResources();
    }

    @Bean
    @ConfigurationProperties("facebook")
    public ClientResources facebook() {
        return new ClientResources();
    }

    private Filter ssoFilter() {
        CompositeFilter compositeFilter = new CompositeFilter();
        List<Filter> filters = new ArrayList<>();
        filters.add(ssoFilter(facebook(), "/login/facebook"));
        filters.add(ssoFilter(github(), "/login/github"));
        compositeFilter.setFilters(filters);
        return compositeFilter;
    }

    private Filter ssoFilter(ClientResources client, String path) {
        OAuth2ClientAuthenticationProcessingFilter oAuth2ClientAuthenticationFilter
                = new OAuth2ClientAuthenticationProcessingFilter(path);
        OAuth2RestTemplate oAuth2RestTemplate
                = new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);
        oAuth2ClientAuthenticationFilter.setRestTemplate(oAuth2RestTemplate);
        UserInfoTokenServices tokenServices
                = new UserInfoTokenServices(client.getResource().getUserInfoUri(), client.getResource().getClientId());
        tokenServices.setRestTemplate(oAuth2RestTemplate);
        oAuth2ClientAuthenticationFilter.setTokenServices(tokenServices);
        oAuth2ClientAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        return oAuth2ClientAuthenticationFilter;
    }
}

class ClientResources {

    @NestedConfigurationProperty
    private AuthorizationCodeResourceDetails client = new AuthorizationCodeResourceDetails();

    @NestedConfigurationProperty
    private ResourceServerProperties resource = new ResourceServerProperties();

    public AuthorizationCodeResourceDetails getClient() {
        return client;
    }

    public ResourceServerProperties getResource() {
        return resource;
    }
}
