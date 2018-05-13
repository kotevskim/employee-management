package com.ecom.martin.emtemployeemanagement.service.impl;

import com.ecom.martin.emtemployeemanagement.model.Employee;
import com.ecom.martin.emtemployeemanagement.model.EmployeeRegisterObject;
import com.ecom.martin.emtemployeemanagement.model.EmployeeVerificationToken;
import com.ecom.martin.emtemployeemanagement.model.exception.EmployeeNotFoundException;
import com.ecom.martin.emtemployeemanagement.model.exception.InvalidTockenException;
import com.ecom.martin.emtemployeemanagement.persistence.EmployeeDao;
import com.ecom.martin.emtemployeemanagement.persistence.EmployeeVerificationTokenDao;
import com.ecom.martin.emtemployeemanagement.service.AuthService;
import com.ecom.martin.emtemployeemanagement.service.EmployeeService;
import com.ecom.martin.emtemployeemanagement.service.MailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final MailService mailService;
    private final EmployeeDao employeeDao;
    private final EmployeeService employeeService;
    private final EmployeeVerificationTokenDao employeeVerificationTokenDao;
    private final PasswordEncoder passwordEncoder;

    private static final String MAIL_SUBJECT_ACCOUNT_ACTIVATION = "Account Activation";
    private static final String MAIL_SUBJECT_PASSWORD_RESET = "Password Reset";

    public AuthServiceImpl(final MailService mailService,
                           EmployeeDao employeeDao, final EmployeeService employeeService,
                           final EmployeeVerificationTokenDao employeeVerificationTokenDao,
                           PasswordEncoder passwordEncoder) {
        this.mailService = mailService;
        this.employeeDao = employeeDao;
        this.employeeService = employeeService;
        this.employeeVerificationTokenDao = employeeVerificationTokenDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerEmployee(EmployeeRegisterObject employee) {
        Employee em = this.employeeService.createEmployee(employee);
        EmployeeVerificationToken verificationToken = new EmployeeVerificationToken(em);
        this.employeeVerificationTokenDao.save(verificationToken);
        String to = em.getEmail();
        String subject = MAIL_SUBJECT_ACCOUNT_ACTIVATION;
        String text = "Copy the flowing code: " + verificationToken.getCode();
        String successLogMessage =  "Mail with activation code sent to " + to;
        mailService.sendMail(to, subject, text, successLogMessage); // async method
    }

    @Override
    @Transactional
    public void verifyEmployee(String activationCode) {
        EmployeeVerificationToken token = this.employeeVerificationTokenDao.findById(activationCode)
                .orElseThrow(InvalidTockenException::new);
        Employee em = this.employeeDao.findByEmail(token.getEmployee().getEmail())
                .orElseThrow(EmployeeNotFoundException::new);
        em.setEnabled(true);
        this.employeeVerificationTokenDao.delete(token);
    }

    @Override
    @Transactional
    public void resetPasswordForEmployee(Employee employee) {
        String newPassword = UUID.randomUUID().toString().substring(0, 6);
        employee.setPassword(passwordEncoder.encode(newPassword));
        String to = employee.getEmail();
        String subject = MAIL_SUBJECT_PASSWORD_RESET;
        String text = String.format("This is your new generated password: %s", newPassword);
        String successLogMessage =  "Mail with new password sent to " + to;
        this.mailService.sendMail(to, subject, text, successLogMessage);
    }

}
