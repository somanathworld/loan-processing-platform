package com.gs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.gs.event.LoanStatusEvent;

@Service
public class NotificationListener {

    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "loan-status-events", groupId = "notification-group")
    public void handleLoanStatus(LoanStatusEvent event) {
        System.out.println("📬 Sending email to customer [" + event.getCustomerId() + "] for loan [" + event.getLoanId() + "] - Status: " + event.getStatus());
        String email = "rm5170741@gmail.com";  // 🔁 In real world, fetch from user-service
        emailService.sendLoanStatusEmail(email, event.getCustomerId(), event.getLoanId(), event.getStatus());
        
    }
}
