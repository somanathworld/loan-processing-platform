package com.gs.notification_service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.gs.event.LoanStatusEvent;
import com.gs.service.NotificationListener;

@SpringBootTest
class NotificationListenerTest {

    @MockitoBean
    private JavaMailSender mailSender;

    @Autowired
    private NotificationListener listener;

    @Test
    void testLoanStatusEmailSent() {
        LoanStatusEvent event = new LoanStatusEvent();
        event.setLoanId("loan123");
        event.setCustomerId("cust123");
        event.setStatus("APPROVED");

        // Mock behavior
        SimpleMailMessage message = new SimpleMailMessage();
        when(mailSender.createMimeMessage()).thenReturn(null);

        listener.handleLoanStatus(event);

        verify(mailSender).send(any(SimpleMailMessage.class));
    }
}
