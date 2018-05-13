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
import org.springframework.mail.MailException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final MailService mailService;
    private final EmployeeDao employeeDao;
    private final EmployeeService employeeService;
    private final EmployeeVerificationTokenDao employeeVerificationTokenDao;

    public AuthServiceImpl(final MailService mailService,
                           EmployeeDao employeeDao, final EmployeeService employeeService,
                           final EmployeeVerificationTokenDao employeeVerificationTokenDao) {
        this.mailService = mailService;
        this.employeeDao = employeeDao;
        this.employeeService = employeeService;
        this.employeeVerificationTokenDao = employeeVerificationTokenDao;
    }

    @Override
    public void registerEmployee(EmployeeRegisterObject employee) {
        Employee em = this.employeeService.createEmployee(employee);
        EmployeeVerificationToken verificationToken = new EmployeeVerificationToken(em);
        this.employeeVerificationTokenDao.save(verificationToken);
        String from = "deutschmankote@gmail.com";
        String to = em.getEmail();
        String subject = "Activate account";
        String text = "Copy the folowing code: " + verificationToken.getCode();
        mailService.sendMail(from, to, subject, text); // async method
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

}
