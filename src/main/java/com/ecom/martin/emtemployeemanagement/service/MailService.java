package com.ecom.martin.emtemployeemanagement.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class MailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendMail(String from, String to, String subject, String text) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(from);
        email.setTo(to);
        email.setSubject(subject);
        email.setText(text);
        boolean sent = false;
        while (!sent) {
            try {
                mailSender.send(email);
                sent = true;
                LOGGER.info("Mail with activation code sent to {}", to);
            } catch (MailException e) {
                LOGGER.warn("Mail could to be sent, will retry. Cause: {}", e.getMessage());
            }
        }
    }
}