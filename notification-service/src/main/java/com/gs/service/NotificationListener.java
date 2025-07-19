package com.gs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.gs.client.UserServiceClient;
import com.gs.dto.CustomerDTO;
import com.gs.event.LoanStatusEvent;

@Service
public class NotificationListener {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserServiceClient userClient;

    @KafkaListener(topics = "loan-status-events", groupId = "notification-group")
    public void handleLoanStatus(LoanStatusEvent event) {

        try {
            // 🔁 Call user-service to get user email
            CustomerDTO user = userClient.getUserInfo(event.getCustomerId());

            System.out.println("📬 Sending email to customer [" + event.getCustomerId() + "] for loan [" + event.getLoanId() + "] - Status: " + event.getStatus());
        
            // 📧 Send email
            emailService.sendLoanStatusEmail(user.getEmail(), event.getCustomerId(), event.getLoanId(), event.getStatus());

            System.out.println("📩 Notification sent to: " + user.getEmail());

        } catch (Exception e) {
            System.err.println("❌ Failed to send notification: " + e.getMessage());
        }      
        
    }
}
