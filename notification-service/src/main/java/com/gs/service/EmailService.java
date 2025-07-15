package com.gs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendLoanStatusEmail(String to, String customerId, String loanId, String status) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@yourapp.test");
        message.setTo(to);
        message.setSubject("Your Loan Application Status");
        message.setText(String.format("Dear Customer %s,\n\nYour loan [%s] has been %s.",
                customerId, loanId, status));
        mailSender.send(message);

        System.out.println("📧 Sent email to: " + to);
    }
}
